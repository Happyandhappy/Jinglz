package com.jinglz.app.data.network.models.log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.jinglz.app.data.network.models.video.VideoResponse;

import java.util.List;

@AutoValue
public abstract class VideoCountLogRequest implements Loggable {

    /**
     * static method with specified videoResponses, to construct new AutoValue_VideoCountLogRequest.
     * first it retrieve list {@code ids} and log video count by constructing
     * {@link AutoValue_VideoCountLogRequest}
     *
     * @param videoResponses list of {@link VideoResponse} to retrieve size and ids of {@code videoResponses}
     * @return VideoCountLogRequest object
     */
    public static VideoCountLogRequest create(List<VideoResponse> videoResponses) {
        final List<String> ids = Stream.of(videoResponses).map(VideoResponse::id).collect(Collectors.toList());

        return new AutoValue_VideoCountLogRequest(videoResponses.size(), ids);
    }

    @SerializedName("Video count")
    public abstract int videoCount();

    @SerializedName("Videos")
    public abstract List<String> videoIds();
}
