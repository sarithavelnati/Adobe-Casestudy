/********************************************************************************************
*   @Author :       Saritha
*   @Purpose:       REST API to expose the accounts that matched with the name                   
*   @Class          FetchAccountByNameRestAPI
*   @CreatedDate:   29-Sep-2016
**********************************************************************************************/
@RestResource(urlMapping='/fetchAccountByName/*')
global with sharing class FetchAccountByNameRestAPI {

    @HttpGet
    global static list<Account> doGet() {
        RestRequest req = RestContext.request;
        RestResponse res = RestContext.response;
        String accountName = req.requestURI.substring(req.requestURI.lastIndexOf('/')+1);
        list<Account> result = [SELECT Id, Name FROM Account where name=:accountName];
        return result;
    }

}