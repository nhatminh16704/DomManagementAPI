package com.domhub.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
//    // Success (1000-1999)
//    SUCCESS(1000, "Success"),
//    NO_CONTENT(1001, "No content"),
//
//    // Client errors (2000-2999)
//    INVALID_REQUEST(2000, "Invalid request"),
//
//    // Auth errors (3000-3999)
//    UNAUTHORIZED(3000, "Unauthorized"),
//    TOKEN_EXPIRED(3001, "Token expired"),
//    FORBIDDEN(3002, "Forbidden"),
//    USER_NOT_FOUND(3003, "User not found"),
//    WRONG_PASSWORD(3004, "Wrong password"),
//    ACCOUNT_NOT_FOUND(3005, "Account not found"),
//    PASSWORD_NOT_MATCH(3006, "Password not match"),
//
//
//
//    // Business errors (4000-4999)
//    DEVICE_NOT_FOUND(4000, "Device not found"),
//    DEVICE_NOT_FOUND_IN_ROOM(4001, "Device not found in room"),
//    DEVICE_ALREADY_EXISTS_IN_ROOM(4002, "Device already exists in room"),
//    ROOM_NOT_FOUND(4003, "Room not found"),
//    MESSAGE_NOT_FOUND(4004, "Message not found"),
//    MESSAGE_TO_NOT_FOUND(4005, "Message recipient not found"),
//    REPORT_NOT_FOUND(4006, "Report not found"),
//    BILLING_NOT_FOUND(4007, "Billing not found"),
//    INVALID_ELECTRICITY_END(4008, "Invalid electric end"),
//    STUDENT_NOT_REGISTERED_ROOM(4009, "Student not registered in room"),
//    STUDENT_NOT_FOUND_WITH_ACCOUNT_ID(4010, "Student not found with account ID"),
//    CANT_REGISTER_ROOM(4011, "Can't register room"),
//    NOT_IN_REGISTRATION_PERIOD(4012, "Not in registration period"),
//    STAFF_NOT_FOUND(4013, "Staff not found"),
//    EMAIL_ALREADY_EXISTS(4014, "Email already exists"),
//    PHONE_ALREADY_EXISTS(4015, "Phone number already exists"),
//    USERNAME_ALREADY_EXISTS(4016, "Username already exists"),
//    ROLE_NOT_FOUND(4017, "Role not found"),
//    STUDENT_NOT_FOUND(4018, "Student not found"),
//    STUDENT_CODE_ALREADY_EXISTS(4019, "Student code already exists"),
//    ROOM_RENTAL_NOT_FOUND(4020, "Room rental not found"),
//    ROOM_RENTAL_NOT_VALID_SATUS(4021, "Room rental not valid status"),
//    ROOM_FULL(4022, "Room full"),
//    ROOM_BILL_NOT_FOUND(4023, "Room bill not found"),
//    ROOM_BILL_ALREADY_PAID(4024, "Room bill is already paid"),
//    INVALID_SIGNATURE(4025, "Invalid signature"),
//
//
//
//    // Server errors (5000-5999)
//    UNCATEGORIZED_EXCEPTION(5000, "Uncategorized exception");


    // Success (1000-1999)
    SUCCESS(1000, "Thành công"),
    NO_CONTENT(1001, "Không có nội dung"),

    // Client errors (2000-2999)
    INVALID_REQUEST(2000, "Yêu cầu không hợp lệ"),

    // Auth errors (3000-3999)
    UNAUTHORIZED(3000, "Chưa xác thực"),
    TOKEN_EXPIRED(3001, "Token đã hết hạn"),
    FORBIDDEN(3002, "Không có quyền truy cập"),
    USER_NOT_FOUND(3003, "Không tìm thấy người dùng"),
    WRONG_PASSWORD(3004, "Sai mật khẩu"),
    ACCOUNT_NOT_FOUND(3005, "Không tìm thấy tài khoản"),
    PASSWORD_NOT_MATCH(3006, "Mật khẩu không khớp"),


    // Business errors (4000-4999)
    DEVICE_NOT_FOUND(4000, "Không tìm thấy thiết bị"),
    DEVICE_NOT_FOUND_IN_ROOM(4001, "Không tìm thấy thiết bị trong phòng"),
    DEVICE_ALREADY_EXISTS_IN_ROOM(4002, "Thiết bị đã tồn tại trong phòng"),
    ROOM_NOT_FOUND(4003, "Không tìm thấy phòng"),
    MESSAGE_NOT_FOUND(4004, "Không tìm thấy tin nhắn"),
    MESSAGE_TO_NOT_FOUND(4005, "Không tìm thấy người nhận tin nhắn"),
    REPORT_NOT_FOUND(4006, "Không tìm thấy báo cáo"),
    BILLING_NOT_FOUND(4007, "Không tìm thấy hóa đơn"),
    INVALID_ELECTRICITY_END(4008, "Chỉ số điện kết thúc không hợp lệ"),
    STUDENT_NOT_REGISTERED_ROOM(4009, "Sinh viên chưa đăng ký phòng"),
    STUDENT_NOT_FOUND_WITH_ACCOUNT_ID(4010, "Không tìm thấy sinh viên với ID tài khoản"),
    CANT_REGISTER_ROOM(4011, "Không thể đăng ký phòng"),
    NOT_IN_REGISTRATION_PERIOD(4012, "Không trong thời gian đăng ký"),
    STAFF_NOT_FOUND(4013, "Không tìm thấy nhân viên"),
    EMAIL_ALREADY_EXISTS(4014, "Email đã tồn tại"),
    PHONE_ALREADY_EXISTS(4015, "Số điện thoại đã tồn tại"),
    USERNAME_ALREADY_EXISTS(4016, "Tên đăng nhập đã tồn tại"),
    ROLE_NOT_FOUND(4017, "Không tìm thấy vai trò"),
    STUDENT_NOT_FOUND(4018, "Không tìm thấy sinh viên"),
    STUDENT_CODE_ALREADY_EXISTS(4019, "Mã sinh viên đã tồn tại"),
    ROOM_RENTAL_NOT_FOUND(4020, "Không tìm thấy đăng ký phòng"),
    ROOM_RENTAL_NOT_VALID_SATUS(4021, "Trạng thái đăng ký phòng không hợp lệ"),
    ROOM_FULL(4022, "Phòng đã đầy"),
    ROOM_BILL_NOT_FOUND(4023, "Không tìm thấy hóa đơn phòng"),
    ROOM_BILL_ALREADY_PAID(4024, "Hóa đơn phòng đã được thanh toán"),
    INVALID_SIGNATURE(4025, "Chữ ký không hợp lệ"),


    // Server errors (5000-5999)
    UNCATEGORIZED_EXCEPTION(5000, "Lỗi không xác định");


    private final int code;
    private final String message;

}
