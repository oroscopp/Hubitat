/**
 *  Design Usage:
 *  This is the 'Parent' app for scheduled switching
 *
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This SmartApp is free!
 *
 *  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this app without a donation, but if you find it useful
 *  then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this app better?  - Please let me know :)
 *
 *  
 *
 *-------------------------------------------------------------------------------------------------------------------
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  If modifying this project, please keep the above header intact and add your comments/credits below - Thank you! -  @Cobra
 *
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  Last Update: 30/07/2018
 *
 *  Changes:
 *
 * 
 *
 *
 *  V1.0.0 - POC
 *
 */



definition(
    name:"Scheduled Switch",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "This is the 'Parent' app for Schedluled Switch",
    category: "Convenience",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: ""
    )







preferences {
	 display()
     page name: "mainPage", title: "", install: true, uninstall: true // ,submitOnChange: true 
     
} 


def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}

def initialize() {
	version()
    log.info "There are ${childApps.size()} child smartapps"
    childApps.each {child ->
    log.info "Child app: ${child.label}"
    }
}
 
 
 
def mainPage() {
    dynamicPage(name: "mainPage") {
      
		section {    
			paragraph image: "",
				title: "Scheduled Switch",
				required: false,
				"This parent app is a container for all schedule switch child apps"
			}
        display()
    
		
  
     
     
// New Child Apps 
      
      
      
        
      section (){
		app(name: "newApp", appName: "Schedule Switch Child", namespace: "Cobra", title: "Schedule a new switch event", multiple: true)
		
            
            }
                  
   
           
           
// End: New Child Apps
  
  
              section() {
                label title: "Enter a name for parent app (optional)", required: false
            }
  
  
  
  
  
 } // DynamicPage 
  
  } // Mainpage




// Check Version   *********************************************************************************
def version(){
    cobra()
    if (state.Type == "Application"){
    schedule("0 0 14 ? * FRI *", cobra)
    }
    if (state.Type == "Driver"){
    schedule("0 45 16 ? * MON *", cobra)
    }
}

def display(){
    
    section{
            paragraph "Version Status: $state.Status"
			paragraph "Current Version: $state.version -  $state.Copyright"
			}

}


def cobra(){
    
    setAppVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       try {
        httpGet(paramsUD) { respUD ->
//   log.info " Version Checking - Response Data: ${respUD.data}"   // Debug Code 
       def copyNow = (respUD.data.copyright)
       state.Copyright = copyNow
            def newver = (respUD.data.versions.(state.Type).(state.InternalName))
            def cobraVer = (respUD.data.versions.(state.Type).(state.InternalName).replace(".", ""))
       def cobraOld = state.version.replace(".", "")
       if(cobraOld < cobraVer){
		state.Status = "<b>** New Version Available (Version: $newver) **</b>"
           log.warn "** There is a newer version of this $state.Type available  (Version: $newver) **"
       }    
       else{ 
      state.Status = "Current"
      log.info "$state.Type is the current version"
       }
       
       }
        } 
        catch (e) {
        log.error "Something went wrong: $e"
    }
}        



 
// App Version   *********************************************************************************
def setAppVersion(){
     state.version = "1.0.0"
     state.InternalName = "SchedSwitchparent"
     state.Type = "Application"
 //  state.Type = "Driver"

}