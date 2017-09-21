<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<body style="background: #000; font-family: Rockwell Extra Bold,Rockwell Bold,monospace; ">
    <div style="background: #000; color: #66ff00; padding: 10px;">
        ${message}
        <div style="background: #009999; color: #ffffff; padding: 10px; margin-left: 40%; width: 20%; border: 2px activeborder groove; ">
            <form autocomplete="off">
                <label style="padding: 7px; width: 100%; text-align: center;" path="msisdn">Please enter your MSISDN number...{Start with 27}</label><br><br>
                <input style="padding: 7px; width: 100%; border: 1px #FBA001 solid; border-radius: 2px;" name="msisdn" value="" required="true"/><br><br>
                <input style="padding: 7px; width: 100%;" type="submit" value="Send Message" formaction="${pageContext.servletContext.contextPath}/language" class="btn btn-primary form-control" role="button" formmethod="GET"/>    
            </form>
        </div>
    </div>
</body>