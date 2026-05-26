-- Dữ liệu mẫu cho demo (chỉ insert nếu chưa có)
IF NOT EXISTS (SELECT 1 FROM books WHERE id = 1)
    INSERT INTO books (title, author, created_at)
    VALUES (N'Spring Security in Action', N'Craig Walls', SYSDATETIME());
