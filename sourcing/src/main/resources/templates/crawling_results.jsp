<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Crawling Results</title>
</head>
<body>
    <h1>Crawling Results</h1>
    <ul>
        <c:forEach items="${crawlingResults}" var="result">
            <li>
                <strong>제목:</strong> ${result.title}<br>
                <strong>링크:</strong> ${result.link}<br>
                <strong>이미지:</strong> <img src="${result.imageUrl}" alt="상품 이미지"><br>
            </li>
        </c:forEach>
    </ul>
</body>
</html>