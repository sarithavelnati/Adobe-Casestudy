/********************************************************************************************
*   @Author :       Saritha
*   @Purpose:       To fetch the Account Details from ORG2 through REST API callout(GET)                   
*   @Page           API_Accounts
*   @Class          AccountIntegrationHandler
*   @CreatedDate:   29-Sep-2016
**********************************************************************************************/
public class AccountIntegrationHandler{

	// Initialize setCon and return a list of records
    public List<Account> getAccountPaginationList() {
        setcon.setPageSize(5);
        return (List<account>) setCon.getRecords();
    }

	// for standard list controllers
    public ApexPages.StandardSetController setCon {
        get {
            if(setCon == null) {
                setCon = new ApexPages.StandardSetController(fetchAccounts());
            }
            return setCon;
        }
        set;
    }
`	
	public static list<Account> fetchAccounts(){
		list<account> accountList = new list<account>();
		Map<string,string> accessTokenMap = IntegrationHelper.oauthLogin('LoginUrl',
            'clientKey',
            'clientSecret', 
            'userName', 
            'password');
            
        String accessToken = accessTokenMap.get('access_token');
              

		// Instantiate a new http object
		Http h = new Http();

		// Instantiate a new HTTP request, specify the method (GET) as well as the endpoint
		HttpRequest req = new HttpRequest();		
		String EndPoint='https://vamc-dev-ed.my.salesforce.com/services/apexrest/fetchAccounts';		
		req.setEndpoint(EndPoint); //ENDPOINT URL 
		req.setMethod('GET'); //TYPE OF METHOD
		req.setHeader('Authorization', 'OAuth '+accessToken);		
		req.setHeader('Content-Type','application/json');
		req.setHeader('Accept','application/json');

		// Send the request, and return a response
		HttpResponse res = h.send(req);
		system.debug('@@ response'+res);
		system.debug('@@ response body'+res.getBody());
		if(!String.isBlank(res.getBody())){        
			accountList  = (list<account>)JSON.deserialize(res.getBody(),list<account>.class);        
		}
		return accountList;
	}


	// returns the previous page of records
    public void previous() {
        setCon.previous();
    }

    // returns the next page of records
    public void next() {
         setCon.next();
    }
	
	// indicates whether there are more records after the current page set.
    public Boolean hasNext {
        get {
            return setCon.getHasNext();
            //return true;
        }
        set;
    }

    // indicates whether there are more records before the current page set.
    public Boolean hasPrevious {
        get {
            return setCon.getHasPrevious();
            //return false;
        }
        set;
    }
    
    public pagereference navigateToCreateAccount(){
        return new pagereference('/apex/CreateAccount');
    }

}