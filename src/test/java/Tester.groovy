//@Grab(group='com.github.groovy-wslite',module='groovy-wslite',version='0.8.0')
import wslite.rest.*
import groovy.json.JsonBuilder

public class Tester implements TestInterface {
    public void printIt() {
        println "this is in the test class";
    }
    
    public void getTrips(){
        def client=new RESTClient("http://139.162.206.200:8080/restapi/trip/100")
        def response = client.get()
        println new JsonBuilder(response.json).toPrettyString()
    }
    
}