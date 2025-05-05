import React, { useEffect, useState } from "react";
import { Form, Input, Button, message, Modal, Row, Col } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import axios from "axios";
import { SetLoading } from "../../app/reducer/common/LoadingSlice.reducer";
import { AuthenticationApi } from "../../api/auth/AuthenticationApi";
import  {jwtDecode} from "jwt-decode";
import { useAppDispatch } from "../../app/hook";
// import { ROLE_ADMIN } from "../../helper/common/constants";
import { useNavigate } from "react-router";

const Login = ({ isVisible, onCancel }) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [errorUsername, setErrorUsername] = useState("");
  const [errorEmail, setErrorEmail] = useState("");
  const [errorPassword, setErrorPassword] = useState("");

  const [isVisibleInternal, setIsVisibleInternal] = useState(isVisible);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    setIsVisibleInternal(isVisible);
    if (isVisible) {
      setUsername("");
      setPassword("");
      setErrorUsername("");
      setErrorPassword("");
    }
  }, [isVisible]);

  //NHO MAIL TAM
  // useEffect(() => {
  //   const savedEmail = localStorage.getItem("email");
  //   if (savedEmail) {
  //     setEmail(savedEmail);
  //   }
  // }, []);

  const [isMobile, setIsMobile] = useState(window.innerWidth <= 768); // neu man hinh < 768 thi isMobile = true
  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768);
    };

    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  const login = () => {
    let check = 0;
    if (!username.trim()) {
      setErrorUsername("Tên người dùng không được để trống");
      check++;
    } else {
      setErrorUsername("");
    }

    if (!password.trim()) {
      setErrorPassword("Mật khẩu không được để trống");
      check++;
    } else {
      setErrorPassword("");
    }

    if (check === 0) {
      dispatch(SetLoading(true));
      let request = {
        //email: email.trim(),
        username: username.trim(),
        password: password.trim(),
      };

      if(username == "admin" && password == "admin"){
        navigate("/admin");
      }
      else {
        navigate("/client");
      }
      message.success("Đăng nhập thành công. Chúc bạn một ngày tốt lành !");
      //localStorage.setItem("email", email.trim());
      // AuthenticationApi.login(request).then((response) => {
      //   dispatch(SetLoading(false));
      //   localStorage.setItem("token", response.data.result.token);
      //   const decodedToken = jwtDecode(response.data.result.token);
      //   const scope = decodedToken.scope || "USER";
      //   if (scope === "") {
      //     navigate("/admin");
      //   } else {
      //     if (isMobile) {
      //       window.location.href = "/client";
      //     } else {
      //       navigate("/client");
      //     }
      //   }
      //   message.success("Đăng nhập thành công. Chúc bạn một ngày tốt lành !");
      // });
    }
  };

  return (
    <>
      <Modal
        title={<h2 style={{ fontFamily: "KronaOne, sans-serif" }}>COSHINE</h2>}
        open={isVisibleInternal}
        onCancel={onCancel}
        footer={[]}
        width={450}
        style={{ textAlign: "center" }}
      >
        {/* <div>
          <p style={{ fontSize: "20px", fontFamily: "Inter, sans-serif" }}>
            Chào mừng bạn đã quay trở lại
          </p>
        </div> */}
        <div>
          <Row>
            <Col span={24} style={{ textAlign: "left", padding: 10 }}>
              <span className="required">(*) </span>
              <span>Tên đăng nhập</span> <br />
              <Input
                type="text"
                id="username"
                placeholder="Mời nhập tên đăng nhập"
                value={username}
                onChange={(e) => {
                  setUsername(e.target.value);
                }}
              />{" "}
              <span className="required">{errorUsername} </span>
            </Col>{" "}
            <Col span={24} style={{ textAlign: "left", padding: 10 }}>
              <span className="required">(*) </span>
              <span>Mật khẩu:</span> <br />
              <Input
                type="password"
                id="password"
                placeholder="Mời nhập mật khẩu"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                }}
              />{" "}
              <span className="required">{errorPassword} </span>
            </Col>
          </Row>
          <Button
            type="primary"
            onClick={login}
            style={{
              marginTop: 20,
              backgroundColor: "var(--primary-dark)",
              borderColor: "var(--primary-dark)",
              // '&:hover': {
              //   backgroundColor: 'var(--primary-light)',
              //   borderColor: 'var(--primary-light)'
              // }
            }}
          >
            Đăng nhập
          </Button>
        </div>
        <Row gutter={[10]} style={{ marginTop: 20 }}>
          <Col span={13}>
            <div style={{ textAlign: "right" }}>Bạn chưa có tài khoản? </div>
          </Col>
          <Col span={11}>
            <div
              style={{
                textAlign: "left",
                color: "var(--primary-dark)",
                cursor: "pointer",
              }}
              onMouseEnter={(e) =>
                (e.target.style.textDecoration = "underline")
              }
              onMouseLeave={(e) => (e.target.style.textDecoration = "none")}
            >
              Đăng kí ngay
            </div>
          </Col>
        </Row>
        <Row style={{ marginTop: 20 }}>
          <Col span={24}>
            <div
              style={{
                textAlign: "center",
                color: "var(--primary-dark)",
                cursor: "pointer",
              }}
              onMouseEnter={(e) =>
                (e.target.style.textDecoration = "underline")
              }
              onMouseLeave={(e) => (e.target.style.textDecoration = "none")}
              //onClick={openModalForgetPassword}
            >
              Quên mật khẩu?
            </div>
          </Col>
        </Row>
      </Modal>

      {/* <ForgotPassword
        isVisible={isVisibleModalForgotPassword}
        onCancel={cancelModalForgetPassword}
      /> */}
    </>
  );
};
export default Login;
