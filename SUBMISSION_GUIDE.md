GitHub Submission Guide
✅ **Working Java Application**: `SimpleWebhookChallenge.java`  
✅ **Executable JAR**: `SimpleWebhookChallenge.jar` (12,287 bytes)  
✅ **Complete Spring Boot Project**: Full Maven structure in `src/` directory  
✅ **Successfully Tested**: Application runs and gets success response  


Result:
SELECT
    p.AMOUNT as SALARY,
    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) as NAME,
    FLOOR(DATEDIFF(CURDATE(), e.DOB) / 365.25) as AGE,
    d.DEPARTMENT_NAME
FROM PAYMENTS p
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE DAY(p.PAYMENT_TIME) != 1
ORDER BY p.AMOUNT DESC
LIMIT 1


Success Response:
{"success":true,"message":"Webhook processed successfully"}



