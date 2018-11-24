package com.hackerkernel.user.sqrfactor.Constants;

public class ServerConstants {

    //BASE
    public static final String IMAGE_URL = "https://sqrfactor.com";             // without the forward slash at the end
    public static final String IMAGE_BASE_URL = "https://sqrfactor.com/";
    public static final String PDF_BASE_URL = IMAGE_BASE_URL;
    public static final String BASE_URL = "https://sqrfactor.com/api/";
    public static final String BASE_URL_COMPETITION = BASE_URL + "competition/";
    public static final String EXISTING_PARTICIPATE_DATA_FOR_EDIT =BASE_URL+"competition/get_participants_list/";
    private static final String BASE_URL_EVENT = BASE_URL + "event/";

    public static  final String BASE_URL_COMPETITION_LINK = IMAGE_BASE_URL +"competition/";

    public static final String LOGIN = BASE_URL + "login";

    public static final String USER_SEARCH = BASE_URL + "usersearch";

    // COMPETITIONS
    //Post
    public static final String WALL_QUESTION_ADD = BASE_URL_COMPETITION + "wall/question/add";
    public static final String WALL_QUESTION_UPDATE = BASE_URL_COMPETITION + "wall/question/update";
    public static final String WALL_QUESTION_DELETE = BASE_URL_COMPETITION + "wall/question/delete";

    public static final String WALL_QUESTION_COMMENT_ADD = BASE_URL_COMPETITION + "wall/question/comment/add";
    public static final String WALL_QUESTION_COMMENT_UPDATE = BASE_URL_COMPETITION + "wall/question/comment/update";
    public static final String WALL_QUESTION_COMMENT_DELETE = BASE_URL_COMPETITION + "wall/question/comment/delete";

    public static final String DELETE_POST_COMPETITION = BASE_URL_COMPETITION + "delete-post-competition";

    public static final String SUBMISSION_LIKE = BASE_URL_COMPETITION + "submission/like";
    public static final String SUBMISSION_DESIGN_SAVE = BASE_URL_COMPETITION + "submission/design-save";
    public static final String POST_JOB = BASE_URL + "job/save";

    public static final String PARTICIPATE_DATA = BASE_URL_COMPETITION + "post/participate-data";
    public static final String COMPETITION_SAVE = BASE_URL_COMPETITION + "competition-save";  // Using multipart

    public static final String ELIGIBILITY_CRITERIA_UPDATE = BASE_URL_COMPETITION + "eligibilityCriteria-awardstherDetails-update";
    public static final String BRIEF_UPDATE = BASE_URL_COMPETITION + "brief-update";
    public static final String TITLE_N_COVER_UPDATE = BASE_URL_COMPETITION + "title-cover-update";
    public static final String JURY_UPDATE = BASE_URL_COMPETITION + "Jury-update";
    public static final String PARTNER_UPDATE = BASE_URL_COMPETITION + "in-association-with-update";

    public static final String SUBMISSIONS_SORT = BASE_URL_COMPETITION + "competitionsort";

    public static final String PAYMENT_CONFIRM = BASE_URL_COMPETITION + "userParticipate/";

    public static final String PAY_PAL = BASE_URL_COMPETITION + "status/";

    public static final String PAY_UMONEY = BASE_URL_COMPETITION + "success";

    //Get
    public static final String COMPETITION = BASE_URL + "competition";
    public static final String PAST_COMPETITION = BASE_URL + "competition/pastcompetition";
    public static final String COMPETITION_DETAIL = COMPETITION + "/";
    public static final String SUBMISSION_ID_DETAILS = COMPETITION;
//    public static final String PARTICIPATE_CHECK = BASE_URL_COMPETITION + "ParticipateCode/";
    public static final String PARTICIPATE_CHECK = "https://archsqr.in/api/competition/participate-check-exist";



    // EVENTS
    //Post
    public static final String EVENT_ADD = BASE_URL_EVENT + "add";   //after clicking next button in event page
    public static final String EVENT_SET_STATE = BASE_URL + "profile/state"; //post the state from the event
    public static final String EVENT_SET_CITY = BASE_URL + "profile/city"; //post the city from the event
    public static final String EVENT_DELETE = BASE_URL_EVENT + "eventsdelete"; //delete the selected event from the list
    public static final String EVENT_2_ADD = BASE_URL_EVENT + "2/add"; //post event2 details
    public static final String EVENT_REGISTER = BASE_URL_EVENT + "apply-event/"; //register in event
    public static final String EVENT_REGISTERED_USER = BASE_URL_EVENT + "view-event-user"; //view registered user in an event


    //Get
    public static final String EVENT_LIST = BASE_URL + "event/list";   //gets the list of all the available event
    public static final String EVENT_GET_COUNTRY = BASE_URL_EVENT + "country"; //gets the country list in the spinner in the event post form
    public static final String EVENT_DETAIL = BASE_URL + "event/"; //for detail of the event selected


    //JOBS
    // Post


    // Get


}
