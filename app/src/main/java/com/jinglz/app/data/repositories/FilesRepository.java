package com.jinglz.app.data.repositories;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.jinglz.app.config.AWSConfig;
import com.jinglz.app.Constants;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.models.UploadFileResponse;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import rx.Single;

@Singleton
public class FilesRepository {

    private static final String TAG = "FilesRepository";

    private final Api mApi;
    private final AmazonS3Client mAmazonS3;
    private final Context mContext;

    /**
     * Constructs new instance of FilesRepository with specified api, amazonS3 and context.
     * This class is basically used to handle files record. Image files will be saved using
     * amazon's S3 services.
     *
     * @param api Api parameter that will be used to download image file
     * @param amazonS3 AmazonS3Client parameter to use for uploading image file
     * @param context for fetching application-specific data
     */
    @Inject
    public FilesRepository(Api api, AmazonS3Client amazonS3, Context context) {
        mApi = api;
        mAmazonS3 = amazonS3;
        mContext = context;
    }

    /**
     * This method is used to retrieve image from specified source, that can be gallery or camera.
     * image file will be retrieved from uri, using {@link RxImageConverters#uriToBitmap(Context, Uri)}.
     *
     * @param source enum to provide source that can be a camera or gallery.
     * @return new instance of File from uri.
     */
    public Single<File> getImageFrom(Sources source) {
        return RxImagePicker.with(mContext)
                .requestImage(source)
                .flatMap(uri -> RxImageConverters.uriToFile(mContext, uri,
                                                            new File(mContext.getCacheDir(), getName(uri))))
                .toSingle();
    }

    /**
     * method with specified file, which is used to upload image file. {@link TransferUtility}
     * instance will be used to upload image file. {@code fileKey} will be used for S3 client weak
     * reference. it will return an new instance of {@link UploadFileResponse}
     *
     * @param file File to be uploaded
     * @return new instance of UploadFileResponse
     */
    public UploadFileResponse uploadImage(File file) {
        TransferUtility transferUtility = new TransferUtility(mAmazonS3, mContext);
        String fileKey = UUID.randomUUID().toString() + getExtension(file.getName());
        return new UploadFileResponse(fileKey, transferUtility.upload(AWSConfig.PROFILES_IMAGES_BUCKET, fileKey,
                                                                  file, CannedAccessControlList.PublicRead));
    }

    /**
     * method with specified id and file, which is used to upload image file for a specific id.
     * {@link TransferUtility} instance will be used to upload image file.
     * {@code fileKey} will be used for S3 client weak reference, that is created using {@code id}
     * and {@link #getExtension(String)}.
     * it will return an new instance of {@link UploadFileResponse}
     *
     * @param id string variable containing specific id to upload file.
     * @param file File to be uploaded
     * @return new instance of UploadFileResponse
     */
    public UploadFileResponse uploadImage(String id, File file) {
        TransferUtility transferUtility = new TransferUtility(mAmazonS3, mContext);
        String fileKey = id + getExtension(file.getName());
        return new UploadFileResponse(fileKey, transferUtility.upload(AWSConfig.PROFILES_IMAGES_BUCKET, fileKey,
                                      file, CannedAccessControlList.PublicRead));
    }

    /**
     * This method is used to download specific file from a specified url.
     * {@link Api#downloadFile(String)} is used to download image file, that will be
     * saved using {@link #writeResponseBodyToDisk(String, ResponseBody)}.
     *
     * @param url String variable that contains image url to be downloaded.
     * @return new instance of File.
     */
    public Single<File> downloadFile(String url) {
        return mApi.downloadFile(url)
                .map(responseBody -> writeResponseBodyToDisk(url, responseBody));
    }

    /**
     * method with specified uri that can be used for retrieving file name.
     *
     * @param uri Uri that will be used to retrieve name
     * @return string variable containing image file name.
     */
    private String getName(Uri uri) {
        Cursor returnCursor =
                mContext.getContentResolver().query(uri, null, null, null, null);
        if (returnCursor == null) {
            return Constants.DEFAULT_FILE_NAME;
        }

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    /**
     * This method is used to retrieve extension of file name.
     * it uses filename as input parameter and retrieve extension by applying string manipulations.
     *
     * @param fileName String variable containing name of the file
     * @return String variable containing extension of the file
     */
    private String getExtension(String fileName) {
        String extension;
        int dotIdx = fileName.lastIndexOf(".");
        if (dotIdx != -1) {
            extension = fileName.substring(dotIdx, fileName.length());
        } else {
            extension = Constants.DEFAULT_FILE_NAME;
        }
        return extension;
    }

    /**
     * method with specified url and responseBody to write save image in file from
     * {@code url}. An IOException can be thrown during this process so it uses try catch
     * block to handle this exception.
     *
     * @param url String variable containing url of image
     * @param responseBody ResponseBody that will be used to retrieve data in byte format
     *                     and length of content
     * @return File object.
     */
    private File writeResponseBodyToDisk(String url, ResponseBody responseBody) {
        try {
            String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            extension = extension.isEmpty() ? Constants.DEFAULT_FILE_EXTENSION : extension;
            final String fileName = UUID.randomUUID().toString() + "." + extension;
            final File file = new File(mContext.getCacheDir(), fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                final byte[] fileReader = new byte[4096];

                final long fileSize = responseBody.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = responseBody.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return file;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

}
