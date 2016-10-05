/********************************************************************************************
*   @Author :       Saritha
*   @Purpose:       REST API to expose the accounts in ORG2                   
*   @Class          FetchAccountsRestAPI
*   @CreatedDate:   29-Sep-2016
**********************************************************************************************/
@RestResource(urlMapping='/fetchAccounts/*')
global with sharing class FetchAccountsRestAPI {

    @HttpGet
    global static list<Account> doGet() {
        RestRequest req = RestContext.request;
        RestResponse res = RestContext.response;
        list<Account> result = [SELECT Id, Name, rating FROM Account order by name ASC];
        return result;
    }

}