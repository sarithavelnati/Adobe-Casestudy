/********************************************************************************************
*   @Author :       Saritha
*   @Purpose:       To create the Account & do a dynamic check in org2 for name and update via Future method
*   @Page           CreateAccount
*   @Class          CreateAccountController
*   @CreatedDate:   29-Sep-2016
**********************************************************************************************/
public class CreateAccountController {
	
	//Declaring Variables
	public  account accRec{get;set;}
	public static account matchedAccount{get;set;}
	
	public CreateAccountController(){
		accRec = new account();
		matchedAccount = new account();
	}
	
	public  void createAccount(){
		insert accRec;  
		updateORG2Reference(accRec.id);    
	}
	
	//Future Callout
	@future(callout=true)
	public static void updateORG2Reference(string accid){
		Map<string,string> accessTokenMap = IntegrationHelper.oauthLogin('LoginUrl',
            'clientKey',
            'clientSecret', 
            'userName', 
            'password');
            
				
		String accessToken = accessTokenMap.get('access_token');
				  
		account accToUpdate = [select id,name,Org2Reference__c from account where id=:accId];
		// Instantiate a new http object
		Http h = new Http();

		// Instantiate a new HTTP request, specify the method (GET) as well as the endpoint
		HttpRequest req = new HttpRequest();		
		String EndPoint='https://vamc-dev-ed.my.salesforce.com/services/apexrest/fetchAccountByName/'+accToUpdate.Name;		
		req.setEndpoint(EndPoint); //ENDPOINT URL 
		req.setMethod('GET'); //TYPE OF METHOD
		req.setHeader('Authorization', 'OAuth '+accessToken);		
		req.setHeader('Content-Type','application/json');
		req.setHeader('Accept','application/json');

		// Send the request, and return a response
		HttpResponse res = h.send(req);
		system.debug('@@ response'+res);
		system.debug('@@ response body'+res.getBody());
		string matchedResponse = res.getBody().substring(1,res.getBody().length()-1);
		
		if(!String.isBlank(res.getBody())){        
			matchedAccount  = (Account)JSON.deserialize(matchedResponse,Account.class);
			if(matchedAccount!=null){

				accToUpdate.Org2Reference__c =matchedAccount.Id;
				update accToUpdate;
			}                    
		}
	}
}