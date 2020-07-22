import { Injectable } from '@angular/core';
import { ChatbotResponse, ChatbotService } from '../chatbot/chatbot-abstract.service';
import { BSAMChatbotAPIResponse } from './bsam-chatbot.model';

@Injectable()
export class BSAMChatbotService implements ChatbotService {

  getChatbotResponse() {

    let apiResponse: BSAMChatbotAPIResponse = {
      query: "where can I find documentation for location api",
      resultText: "Overview\n\n\n\nThe Public Location Services API provides Location Services data (Country, State, City, and ZIP) and is to be used when validating location data being submitted to Beta.SAM.gov ONLY. Beta.SAM is not the authoritative source for location data and is agglomerating data from 3rd",
      documentLink: "https://s3.us-east-1.amazonaws.com/documentsearchbot-931836355907-documentstore/Location_API_Index.html",
      documentTitle: "Beta.SAM.Gov Public Location Services API | GSA Open Technology"
    };

    //apiResponse = {"query":"what are query string parameters","resultText":"All the optional search filters can be sent in the request URL or in the \"Body\".\n\n\n\n\n\n\n\n\nQuery String Parameters\n\tParameter Name\tDescription\tApplicable Versions\n\tsamRegistered\tAllows Yes, No or All. \n\n  Yes signifies SAM registrants.\n No signifies non-SAM registrants.\n\n  All signifies both SAM r","documentLink":"https://s3.us-east-1.amazonaws.com/documentsearchbot-931836355907-documentstore/EM_API_Index.html","documentTitle":"Beta.SAM.Gov Entity Management API | GSA Open Technology"};

    let responseHTML: string = "";
    if (this.hasValue(apiResponse.resultText)) {
      responseHTML += apiResponse.resultText + "...";
    }

    if (this.hasValue(apiResponse.documentLink)) {
      if (responseHTML.length > 0) {
        responseHTML += "\n\n";
      }

      let info = "Please open the document below for more information:";
      let docLink = apiResponse.documentLink;
      let docTitle = this.hasValue(apiResponse.documentTitle) ? apiResponse.documentTitle : 'External Link';

      let template = `<div class="usa-link-wrapper"><p>${info}</p><p><a class="usa-link" href="${docLink}">${docTitle}</fa-icon></a></p></div>`;
      //<i class="fas fa-external-link-alt fa-xs"></i></a>
      //<fa-icon [icon]="['fas', 'external-link-alt']" size="xs">

      responseHTML += template;

    } else if (this.hasValue(apiResponse.documentTitle)) {
      if (responseHTML.length > 0) {
        responseHTML += "\n\n";
      }
      responseHTML += apiResponse.documentTitle;
    }



    /*

{"query":"when is beta sam going live","resultText":"2020 December","documentLink":null,"documentTitle":null}
{"query":"can I edit system account after submitting it","resultText":"No, the change request has to be rejected by the approver, or deleted by either the person who created the account or the System Account Administrator.","documentLink":null,"documentTitle":null}
{"query":"what is em api","resultText":" The Entity Management API will allow users to request Public Entity Information based on various optional request parameters.","documentLink":null,"documentTitle":null}
{"query":"what are query string parameters","resultText":"All the optional search filters can be sent in the request URL or in the \"Body\".\n\n\n\n\n\n\n\n\nQuery String Parameters\n\tParameter Name\tDescription\tApplicable Versions\n\tsamRegistered\tAllows Yes, No or All. \n\n  Yes signifies SAM registrants.\n No signifies non-SAM registrants.\n\n  All signifies both SAM r","documentLink":"https://s3.us-east-1.amazonaws.com/documentsearchbot-931836355907-documentstore/EM_API_Index.html","documentTitle":"Beta.SAM.Gov Entity Management API | GSA Open Technology"}
{"query":"where can I find documentation for location api","resultText":"Overview\n\n\n\nThe Public Location Services API provides Location Services data (Country, State, City, and ZIP) and is to be used when validating location data being submitted to Beta.SAM.gov ONLY. Beta.SAM is not the authoritative source for location data and is agglomerating data from 3rd","documentLink":"https://s3.us-east-1.amazonaws.com/documentsearchbot-931836355907-documentstore/Location_API_Index.html","documentTitle":"Beta.SAM.Gov Public Location Services API | GSA Open Technology"} (edited) 
    */

    const res: ChatbotResponse = { responseHTML: responseHTML };
    return res;
  }

  private prepareHTML(str: string) {

  }

  private hasValue(str: string) {
    return str !== undefined && str !== null && str.length > 0;
  }
}