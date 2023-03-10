package com.project.apidbtester.testapis.services;

import com.project.apidbtester.clientdbinfo.ClientDBCredentialsEntity;
import com.project.apidbtester.clientdbinfo.ClientDBInfoRepository;
import com.project.apidbtester.constants.GlobalConstants;
import com.project.apidbtester.testapis.dtos.TestInput;
import com.project.apidbtester.testapis.dtos.TestResponse;
import com.project.apidbtester.testapis.entities.TestCaseDetails;
import com.project.apidbtester.testapis.repositories.TestCaseDetailsRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Service
public class DeleteApiService {
    @Autowired
    ClientDBInfoRepository clientDBInfoRepository;

    @Autowired
    private TestCaseDetailsRepository testCaseDetailsRepository;

    public TestResponse fetchTestResult(TestInput testInput) {

        TestCaseDetails testCaseDetails = testInput.getTestCaseDetails();
        TestResponse testResponse = new TestResponse();

        try {
            RequestSpecification request = RestAssured.given();
            request.contentType(ContentType.JSON);
            request.baseUri(testCaseDetails.getUrl());
            request.body("");
            Response r = request.delete();
            testResponse.setHttpStatusCode(r.statusCode());
            testCaseDetails.setHttpStatusCode(r.statusCode());

            if (r.statusCode() != HttpStatus.OK.value()) {
                testResponse.setHttpErrorMsg(r.statusLine());
                testResponse.setHttpErrorMsg(r.body().print());
                testCaseDetails.setPassed(false);
                testCaseDetails.setHttpErrorMsg(r.getBody().print());
                testCaseDetailsRepository.save(testCaseDetails);
                return testResponse;
            }

            ClientDBCredentialsEntity clientDBCredentials = clientDBInfoRepository.findById(GlobalConstants.DB_CREDENTIALS_ID).orElseThrow();

            Class.forName(GlobalConstants.JDBC_DRIVER);
            Connection connection = DriverManager
                    .getConnection(clientDBCredentials.getDatabaseUrl(), clientDBCredentials.getUserName(), clientDBCredentials.getPassword());

            Statement statement = connection.createStatement();

            StringBuilder query = new StringBuilder("select count(*)");

            query.append(" from ")
                    .append(testCaseDetails.getTableName())
                    .append(" where ")
                    .append(testCaseDetails.getPrimaryKeyName())
                    .append(" = ")
                    .append(testCaseDetails.getPrimaryKeyValue())
                    .append(";");

            ResultSet result = statement.executeQuery(String.valueOf(query));
            result.next();
            boolean testPassed = false;

            if (result.getInt("count(*)")==0) {
                testPassed = true;
            }

            testResponse.setAllTestPassed(testPassed);
            testCaseDetails.setPassed(testPassed);

            connection.close();
            testCaseDetailsRepository.save(testCaseDetails);

            return testResponse;
        } catch (Exception e) {
            if (e instanceof ConnectException) {
                testResponse.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
                testResponse.setHttpErrorMsg("Unable to call api");
            } else {
                testResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                testResponse.setHttpErrorMsg(e.getMessage());
            }
            return testResponse;
        }
    }
}