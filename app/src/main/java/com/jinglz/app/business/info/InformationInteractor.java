package com.jinglz.app.business.info;

import com.jinglz.app.data.network.models.ContactUsRequest;
import com.jinglz.app.data.network.models.ContactUsResponse;
import com.jinglz.app.data.repositories.DeviceRepository;
import com.jinglz.app.data.repositories.TextRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;
import rx.schedulers.Schedulers;

@Singleton
public class InformationInteractor {

    private final TextRepository mTextRepository;
    private final DeviceRepository mDeviceRepository;

    /**
     * Constructs new InformationInteractor with specified textRepository and deviceRepository.
     *
     * @param textRepository records terms, conditions and other text repository
     * @param deviceRepository keeps track of device related information like unique token
     */
    @Inject
    public InformationInteractor(TextRepository textRepository, DeviceRepository deviceRepository) {
        mTextRepository = textRepository;
        mDeviceRepository = deviceRepository;
    }

    /**
     * return detail steps for how application works. calls {@link TextRepository#getHowItWorks()}
     *
     * @return sequence of characters
     */
    public CharSequence getHowItWorks() {
        return mTextRepository.getHowItWorks();
    }

    /**
     * return terms and conditions for the application. calls {@link TextRepository#getTermsAndConditions()}
     *
     * @return Single of type String
     */
    public Single<String> getTermsAndConditions() {
        return mTextRepository.getTermsAndConditions()
                .subscribeOn(Schedulers.io());
    }

    /**
     * return privacy policies for the application. calls {@link TextRepository#getPrivacyPolicy()}
     *
     * @return Single object of String
     */
    public Single<String> getPrivacyPolicy() {
        return mTextRepository.getPrivacyPolicy()
                .subscribeOn(Schedulers.io());
    }

    /**
     * method with specified message to contact and send feedback regarding application.
     * first it retrieves information of device using {@link DeviceRepository#getDeviceInfo()}
     * then send contact us request b calling {@link TextRepository#sendContactUsRequest(ContactUsRequest)}
     *
     * @param message String variable to send
     * @return Single object of ContactUsResponse
     */
    public Single<ContactUsResponse> sendContactUsMessage(String message) {
        return mDeviceRepository.getDeviceInfo()
                .map(environmentInfo -> ContactUsRequest.create(message, environmentInfo))
                .flatMap(mTextRepository::sendContactUsRequest)
                .subscribeOn(Schedulers.io());
    }

}
