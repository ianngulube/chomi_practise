<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<body style="background: #000; font-family: Rockwell Extra Bold,Rockwell Bold,monospace; ">
    <div style="background: #000; padding: 15px;">
        <div style="background: #000; color: #66ff00; padding: 10px; margin-left: 40%; width: 20%; border: 2px activeborder groove; height: 300px; word-wrap: break-word;">
            <request>
                <headertext>${pageHeader}</headertext><br>
                <options>
                    <c:forEach var="myVar" items="${myItems}">
                        <c:if test="${myVar.showOptions}">
                            <option command="${myVar.command}" order="${myVar.order}" callback="${myVar.url}/${myVar.page}" display="${myVar.display}">${myVar.command}. ${myVar.code}</option>
                        </c:if>
                    </c:forEach>
                </options>
            </request>
        </div>
        <div style="background: #009999; color: #ffffff; padding: 10px; margin-left: 40%; width: 20%; border: 2px activeborder groove; ">
            <form autocomplete="off">
                <label path="request" style="padding: 7px; width: 100%; text-align: center;">Reply Message</label><br>
                <input name="request" style="padding: 7px; width: 100%; border: 1px #FBA001 solid; border-radius: 2px;"/><br><br>
                <input name="msisdn" value="${ussdSession.msisdn}" style="display: none;" required="true"/>
                <input type="submit" value="Send Message" formaction="${pageContext.servletContext.contextPath}/${ussdSession.nextPage}" class="btn btn-primary form-control" role="button" formmethod="GET" style="padding: 7px; width: 100%;"/>
            </form>
        </div>
    </div>
</body>