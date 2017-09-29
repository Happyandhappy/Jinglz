package com.jinglz.app.data.repositories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.models.ContestLeadershipRecord;
import com.jinglz.app.data.network.models.ContestResult;
import com.jinglz.app.data.network.models.DataResponse;
import com.jinglz.app.data.network.models.contest.ContestResponse;
import com.jinglz.app.data.network.models.contest.TakePartInContestRequest;
import com.jinglz.app.data.network.models.video.VideoResponse;
import com.jinglz.app.injection.session.PerSession;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

@PerSession
public class ContestRepository {

    private final Api mApi;
    private final Gson mGson;

    /**
     * String containing result for mock in Json format. it is used in
     * {@link #getMockData()} for retrieving list of {@link ContestLeadershipRecord}.
     */
    public static final String MOCK = "{\"data\":[{\"user\":{\"_id\":\"57bbfd384ce3af1100b34437\","
            + "\"gender\":\"Male\",\"lastName\":\"Cena\",\"firstName\":\"Sean\",\"image\":\"https://s3.amazonaws"
            + ".com/jinglz-images-profiles/57bbfd384ce3af1100b34437.png\"},\"amount\":50,\"position\":1},"
            + "{\"user\":{\"_id\":\"577ecbd9d3692b1100811587\",\"gender\":\"Female\",\"lastName\":\"Chernaya\","
            + "\"firstName\":\"Olesya\",\"image\":\"https://s3.amazonaws"
            + ".com/jinglz-images-profiles/577ecbd9d3692b1100811587.png\"},\"amount\":35,\"position\":2},"
            + "{\"user\":{\"_id\":\"579eed808397d21100038c52\",\"lastName\":\"Kolesnik\",\"gender\":\"Male\","
            + "\"firstName\":\"Yuriy\",\"image\":\"https://s3.amazonaws"
            + ".com/jinglz-images-profiles/579eed808397d21100038c52.png\"},\"amount\":25,\"position\":3},"
            + "{\"user\":{\"_id\":\"57f3fb3493d59d11003c87ca\",\"lastName\":\"Palmer\",\"gender\":\"Male\","
            + "\"firstName\":\"Michael\",\"image\":null},\"amount\":20,\"position\":4},"
            + "{\"user\":{\"_id\":\"5796d7c2ecae62120030741d\",\"gender\":\"Female\",\"lastName\":\"Boyce\","
            + "\"firstName\":\"Andrea\",\"image\":null},\"amount\":15,\"position\":5},"
            + "{\"user\":{\"_id\":\"57c23911260d4e11004769a0\",\"lastName\":\"Shepherd\",\"gender\":\"Male\","
            + "\"firstName\":\"Danny\",\"image\":null},\"amount\":13,\"position\":6},"
            + "{\"user\":{\"_id\":\"57edc93ee9b8c1110095e739\",\"gender\":\"Female\",\"lastName\":\"Runge\","
            + "\"firstName\":\"Joy\",\"image\":null},\"amount\":11,\"position\":7},"
            + "{\"user\":{\"_id\":\"57f1143293d59d11003c7b44\",\"gender\":\"Female\",\"lastName\":\"Hong\","
            + "\"firstName\":\"Nhung\",\"image\":null},\"amount\":11,\"position\":8},"
            + "{\"user\":{\"_id\":\"5773225793b6dd120032bcf5\",\"firstName\":\"Aaliyah\",\"gender\":\"Female\","
            + "\"lastName\":\"R\",\"image\":\"https://s3.amazonaws"
            + ".com/jinglz-images-profiles/5773225793b6dd120032bcf5.png\"},\"amount\":10,\"position\":9},"
            + "{\"user\":{\"_id\":\"57aec6d0221edc1100eb43e5\",\"lastName\":\"Norvick\",\"firstName\":\"Greg\","
            + "\"gender\":\"Male\",\"image\":null},\"amount\":10,\"position\":10},"
            + "{\"user\":{\"_id\":\"5829ddd6594b46a27824ae98\",\"firstName\":\"Max\",\"lastName\":\"Baranov\","
            + "\"image\":null},\"amount\":1,\"position\":71}]}";

    /**
     * String containing result for mock contest in Json format. it is used in
     * {@link #getMockContestResults()} for retrieving list of {@link ContestResult}.
     */
    public static final String MOCK_CONTEST_RESULT = "{\n"
            + " \"data\": [\n"
            + "   {\n"
            + "     \"_id\": \"56d62d002c776a0e005d5097\",\n"
            + "     \"amount\": 50,\n"
            + "     \"position\": 1,\n"
            + "     \"createdAt\": \"2016-03-02T00:00:00.048Z\"\n"
            + "   },\n"
            + "   {\n"
            + "     \"_id\": \"56d63b102c776a0e005d5099\",\n"
            + "     \"amount\": 50,\n"
            + "     \"position\": 1,\n"
            + "     \"createdAt\": \"2016-03-02T01:00:00.021Z\"\n"
            + "   }\n"
            + " ]\n"
            + "}";

    /**
     * construct new ContestRepository with specified api and gson.
     *
     * @param api
     * @param gson
     */
    @Inject
    public ContestRepository(Api api, Gson gson) {
        mApi = api;
        mGson = gson;
    }

    /**
     * method with specified contestId. This method is used to retrieve contest ranking according
     * to {@code contestId}. it use {@link Api#getContestUserRanking(String)} and returns a list
     * of {@link ContestLeadershipRecord}
     *
     * @param contestId String variable that contains contestId.
     * @return list of ContestLeadershipRecord
     */
    public Single<List<ContestLeadershipRecord>> getContestUserRanking(String contestId) {
        return mApi.getContestUserRanking(contestId)
                .map(DataResponse::data);
    }

    /**
     * This method is used to retrieve list of Contest which are available.
     * List contains objects of {@link ContestResult} type.
     *
     * @return list of ContestResult
     */
    public Single<List<ContestResult>> getContestResults() {
        return mApi.getContestResults()
                .map(DataResponse::data);
    }

    /**
     * method with specified id to retrieve contest details of specific id.
     * {@code id} will be used to retrieve response using {@link Api#contest(String)} and
     * save in {@link ContestResult}
     *
     * @param id String variable contains contest id
     * @return ContestResult object
     */
    public Single<ContestResponse> getContest(String id) {
        return mApi.contest(id)
                .map(DataResponse::data);
    }

    /**
     * This method is used to send request for taking part in contest.
     * method contains specified request {@link TakePartInContestRequest} containing videoId and contestId.
     * {@code request} will be passed as an input parameter for {@link Api#takePartInContest(TakePartInContestRequest)}
     * and return its corresponding response.
     *
     * @param request TakePartInContestRequest instance to send request.
     * @return Completable object containing response from api
     */
    public Completable takePartInContest(TakePartInContestRequest request) {
        return mApi.takePartInContest(request)
                .toCompletable();
    }

    /**
     * method is used to retrieve the list of contests which are closed now.
     * it calls {@link Api#closedContests()} and return list of {@link VideoResponse}
     *
     * @return list VideoResponse
     */
    public Single<List<VideoResponse>> getClosedContests() {
        return mApi.closedContests()
                .map(DataResponse::data);
    }


    private Single<DataResponse<List<ContestLeadershipRecord>>> getMockData() {
        final Type type = new TypeToken<DataResponse<List<ContestLeadershipRecord>>>() {
        }.getType();
        return Single.just(mGson.fromJson(MOCK, type));
    }


    private Single<DataResponse<List<ContestResult>>> getMockContestResults() {
        final Type type = new TypeToken<DataResponse<List<ContestResult>>>() {
        }.getType();
        return Single.just(mGson.fromJson(MOCK_CONTEST_RESULT, type));
    }

}
