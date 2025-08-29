GitHub Submission Guide

✅ **Working Java Application**: `SimpleWebhookChallenge.java`  
✅ **Executable JAR**: `SimpleWebhookChallenge.jar` (12,287 bytes)  
✅ **Complete Spring Boot Project**: Full Maven structure in `src/` directory  
✅ **Successfully Tested**: Application runs and gets success response  

**Result**:
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

**Success Response**:
{"success":true,"message":"Webhook processed successfully"}


**Execution Details**:
Registration: 22BCE8419
Question Type: Question 1 (19 is odd)
Problem: Find highest salary NOT credited on 1st day of month
JWT Token: Successfully received and used
Submission:  "Webhook processed successfully"
**What This Query Does**:
Joins three tables: PAYMENTS → EMPLOYEE → DEPARTMENT
Filters transactions: WHERE DAY(p.PAYMENT_TIME) != 1 (excludes 1st day)
Finds highest salary: ORDER BY p.AMOUNT DESC LIMIT 1
Returns required columns:
SALARY: Payment amount
NAME: Combined first + last name
AGE: Calculated from DOB
DEPARTMENT_NAME: Department name


