-- Query showing students with fewer than four activities from a basic program
SELECT s.name
FROM students s
         LEFT JOIN selections sel ON sel.student_id = s.id
         LEFT JOIN basic_courses bc ON bc.id = sel.basic_course_id
GROUP BY s.id, s.name
HAVING COUNT(bc.id) < 4;