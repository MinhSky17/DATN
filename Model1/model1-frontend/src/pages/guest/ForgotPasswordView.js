// import React, { useEffect, useState } from "react";
// import { Button, Modal, Form, Input, message, Flex, Row, Col } from "antd";
// import { useForm } from "antd/es/form/Form";
// import { ForgotPasswordApi } from "../../api/auth/ForgotPasswordApi";
// import { useAppDispatch } from "../../app/hook";
// import { SetLoading } from "../../app/reducer/common/LoadingSlice.reducer";
// import PopupRegisterSuccess from "./PopupRegisterSuccess";

// const checkPassword = (_, value) => {
//   // Yêu cầu mật khẩu: ít nhất 8 ký tự, tối đa 15 ký tự,
//   // ít nhất một chữ cái viết hoa, một chữ số, và một ký tự đặc biệt
//   const regex =
//     /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,15}$/;
//   if (!value || regex.test(value)) {
//     return Promise.resolve();
//   }
//   return Promise.reject(
//     "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ cái viết hoa, số và ký tự đặc biệt."
//   );
// };

// const ForgotPassword = ({ isVisible, onCancel }) => {
//   const [form] = useForm();
//   const dispatch = useAppDispatch();
//   const [isVisibleModalSuccess, setIsVisibleModalSuccess] = useState(false);
//   const [showOtpForm, setShowOtpForm] = useState(false);
//   const [otpTimeout, setOtpTimeout] = useState(120);

//   const [email, setEmail] = useState("");
//   const [password, setPassword] = useState("");
//   const [confirmPassword, setConfirmPassword] = useState("");
//   const [otp, setOtp] = useState("");

//   useEffect(() => {
//     if (isVisible) {
//       form.resetFields();
//       setShowOtpForm(false);
//       setOtpTimeout(120);
//     }
//   }, [isVisible, form]);

//   useEffect(() => {
//     let timer;
//     if (showOtpForm && otpTimeout > 0) {
//       timer = setInterval(() => setOtpTimeout((prev) => prev - 1), 1000);
//     }
//     return () => clearInterval(timer);
//   }, [showOtpForm, otpTimeout]);

//   const handleApiCall = (data, successCallback) => {
//     dispatch(SetLoading(true));
//     ForgotPasswordApi.updatePassword(data).then(
//       (res) => {
//         dispatch(SetLoading(false));
//         successCallback();
//       },
//       (err) => {
//         dispatch(SetLoading(false));
//       }
//     );
//   };

//   const handleGetOTP = () => {
//     handleApiCall({ email, password: null, otp: null }, () => {
//       setShowOtpForm(true);
//       setOtp("");
//       form.resetFields();
//       setOtpTimeout(120);
//     });
//   };

//   const handleSubmitOTP = () => {
//     handleApiCall({ email, password, otp }, () => {
//       form.resetFields();
//       onCancel();
//       message.success("Đổi mật khẩu thành công");
//     });
//   };

//   const renderOtpForm = () => (
//     <Form layout="vertical" style={{ width: "90%" }} onFinish={handleSubmitOTP}>
//       <Form.Item name="otp">
//         <Row justify="center">
//           <p>Nhập mã OTP</p>
//         </Row>
//         <Row justify="center">
//           <Col>
//             <Flex gap="middle" align="flex-start" vertical>
//               <Input.OTP
//                 formatter={(str) => str.toUpperCase()}
//                 value={otp}
//                 onChange={setOtp}
//                 length={6}
//               />
//             </Flex>
//           </Col>
//         </Row>
//       </Form.Item>
//       <p>
//         {otpTimeout > 0 ? (
//           <>
//             Mã <span style={{ color: "#ff9d00", fontWeight: "bold" }}>OTP</span>{" "}
//             sẽ hết hạn sau{" "}
//             <span style={{ color: "red", fontWeight: "bold" }}>
//               {otpTimeout} giây
//             </span>
//           </>
//         ) : (
//           <>
//             Bạn chưa nhận được mã?{" "}
//             <a
//               href="#"
//               onClick={(e) => {
//                 e.preventDefault();
//                 handleGetOTP();
//               }}
//               style={{ textDecoration: "none" }}
//               onMouseOver={(e) => (e.target.style.textDecoration = "underline")}
//               onMouseOut={(e) => (e.target.style.textDecoration = "none")}
//             >
//               Gửi lại mã OTP
//             </a>
//           </>
//         )}
//       </p>
//       <Form.Item>
//         {otpTimeout > 0 && (
//           <Button type="primary" htmlType="submit" disabled={otp.length !== 6}>
//             Xác thực OTP
//           </Button>
//         )}
//       </Form.Item>
//     </Form>
//   );

//   const renderPasswordForm = () => (
//     <Form
//       form={form}
//       layout="vertical"
//       style={{ width: "90%" }}
//       onFinish={handleGetOTP}
//     >
//       <Form.Item
//         label="Địa chỉ email"
//         name="email"
//         rules={[{ required: true, message: "Vui lòng nhập email!" }]}
//       >
//         <Input
//           type="email"
//           placeholder="Địa chỉ email"
//           value={email}
//           onChange={(e) => setEmail(e.target.value)}
//         />
//       </Form.Item>
//       <div style={{ textAlign: "left", marginBottom: 15, color: "red" }} className="text-dark-mode">
//         (*) Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ cái viết hoa, số và ký
//         tự đặc biệt.
//       </div>
//       <Form.Item
//         label="Mật khẩu mới"
//         name="newPassword"
//         rules={[
//           { required: true, message: "Vui lòng nhập mật khẩu mới!" },
//           { validator: checkPassword },
//         ]}
//       >
//         <Input.Password
//           placeholder="Mật khẩu mới"
//           value={password}
//           onChange={(e) => setPassword(e.target.value)}
//         />
//       </Form.Item>
//       <Form.Item
//         label="Nhập lại mật khẩu mới"
//         name="confirmPassword"
//         dependencies={["newPassword"]}
//         rules={[
//           { required: true, message: "Vui lòng nhập lại mật khẩu mới!" },
//           ({ getFieldValue }) => ({
//             validator(_, value) {
//               if (!value || getFieldValue("newPassword") === value)
//                 return Promise.resolve();
//               return Promise.reject(new Error("Mật khẩu nhập lại không khớp!"));
//             },
//           }),
//         ]}
//       >
//         <Input.Password
//           placeholder="Nhập lại mật khẩu mới"
//           value={confirmPassword}
//           onChange={(e) => setConfirmPassword(e.target.value)}
//         />
//       </Form.Item>
//       <Form.Item>
//         <Button type="primary" htmlType="submit">
//           Gửi yêu cầu
//         </Button>
//       </Form.Item>
//     </Form>
//   );

//   return (
//     <>
//       <Modal
//         title={<h2 style={{ fontFamily: "KronaOne, sans-serif" }}>TCT FUND</h2>}
//         visible={isVisible}
//         footer={null}
//         width={450}
//         onCancel={onCancel}
//         style={{ textAlign: "center" }}
//       >
//         <p style={{ fontSize: "20px", fontFamily: "Inter, sans-serif" }}>
//           {showOtpForm
//             ? "Mã xác thực đã được gửi qua Email"
//             : "Đổi mật khẩu của bạn"}
//         </p>
//         <div
//           style={{
//             display: "flex",
//             flexDirection: "column",
//             alignItems: "center",
//           }}
//         >
//           {showOtpForm ? renderOtpForm() : renderPasswordForm()}
//         </div>
//       </Modal>
//       <PopupRegisterSuccess
//         isVisible={isVisibleModalSuccess}
//         onCancel={() => setIsVisibleModalSuccess(false)}
//       />
//     </>
//   );
// };

// export default ForgotPassword;
