SELECT DATE_FORMAT((CURDATE() - DATE(data)), '%D') FROM chat.Messages ORDER BY data DESC LIMIT 1;