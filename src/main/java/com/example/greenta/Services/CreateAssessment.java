package com.example.greenta.Services;

import com.google.cloud.recaptchaenterprise.v1.RecaptchaEnterpriseServiceClient;
import com.google.recaptchaenterprise.v1.Assessment;
import com.google.recaptchaenterprise.v1.CreateAssessmentRequest;
import com.google.recaptchaenterprise.v1.Event;
import com.google.recaptchaenterprise.v1.ProjectName;
import com.google.recaptchaenterprise.v1.RiskAnalysis.ClassificationReason;
import java.io.IOException;

public class CreateAssessment {

    public static void main(String[] args) throws IOException {
        // TO-DO: Replace the token and reCAPTCHA action variables before running the sample.
        String projectID = "greenta-1714603281660";
        String recaptchaKey = "6LeXctIpAAAAAEqpKcFWZBqkltJD-bhe6EiMR_ZR";
        String token = "action-token";
        String recaptchaAction = "action-name";

        createAssessment(projectID, recaptchaKey, token, recaptchaAction);
    }

    /**
     * Create an assessment to analyse the risk of a UI action.
     *
     * @param projectID : Your Google Cloud project ID.
     * @param recaptchaKey : The reCAPTCHA key associated with the site/app
     * @param token : The generated token obtained from the client.
     * @param recaptchaAction : Action name corresponding to the token.
     */
    public static void createAssessment(
            String projectID, String recaptchaKey, String token, String recaptchaAction)
            throws IOException {
        // Create the reCAPTCHA client.
        // TODO: Cache the client generation code (recommended) or call client.close() before exiting the method.
        try (RecaptchaEnterpriseServiceClient client = RecaptchaEnterpriseServiceClient.create()) {

            // Set the properties of the event to be tracked.
            Event event = Event.newBuilder().setSiteKey(recaptchaKey).setToken(token).build();

            // Build the assessment request.
            CreateAssessmentRequest createAssessmentRequest =
                    CreateAssessmentRequest.newBuilder()
                            .setParent(ProjectName.of(projectID).toString())
                            .setAssessment(Assessment.newBuilder().setEvent(event).build())
                            .build();

            Assessment response = client.createAssessment(createAssessmentRequest);

            // Check if the token is valid.
            if (!response.getTokenProperties().getValid()) {
                System.out.println(
                        "The CreateAssessment call failed because the token was: "
                                + response.getTokenProperties().getInvalidReason().name());
                return;
            }

            // Check if the expected action was executed.
            if (!response.getTokenProperties().getAction().equals(recaptchaAction)) {
                System.out.println(
                        "The action attribute in reCAPTCHA tag is: "
                                + response.getTokenProperties().getAction());
                System.out.println(
                        "The action attribute in the reCAPTCHA tag "
                                + "does not match the action ("
                                + recaptchaAction
                                + ") you are expecting to score");
                return;
            }

            // Get the risk score and the reason(s).
            // For more information on interpreting the assessment, see:
            // https://cloud.google.com/recaptcha-enterprise/docs/interpret-assessment
            for (ClassificationReason reason : response.getRiskAnalysis().getReasonsList()) {
                System.out.println(reason);
            }

            float recaptchaScore = response.getRiskAnalysis().getScore();
            System.out.println("The reCAPTCHA score is: " + recaptchaScore);

            // Get the assessment name (ID). Use this to annotate the assessment.
            String assessmentName = response.getName();
            System.out.println(
                    "Assessment name: " + assessmentName.substring(assessmentName.lastIndexOf("/") + 1));
        }
    }
}