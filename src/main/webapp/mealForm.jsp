<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Meal information</title>
</head>
<body>

<h2>Добавление еды</h2>

<form action="meals" method="post">
    <%--<jsp:useBean id="meal" class="ru.javawebinar.topjava.model.Meal"/>--%>
    <input type="hidden" style="display: none" id="id" name="id" value="${meal eq null ? "" : meal.id}"/>

    <label>
        Дата и время:<input required type="datetime-local" name="dt" value="${meal eq null ? "" : meal.dateTime}"/>
    </label>
    <label>
        Описание:<input type="text" name="description" value="${meal eq null ? "" : meal.description}" autofocus required/>
    </label>
    <label>
        Калории:<input type="number" step="any" name="calories" value="${meal eq null ? "" : meal.calories}" required/>
    </label>

    <input type="submit" value="OK"/>
</form>

</body>
</html>
