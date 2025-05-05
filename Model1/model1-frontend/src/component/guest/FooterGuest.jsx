import { Col, Dropdown, Menu, Row } from "antd";
import "./style-footer.css";
import iconFacebook from "../../assets/img/facebook.png";
import iconZalo from "../../assets/img/zalo.png";
import iconTelegram from "../../assets/img/telegram.png";
import { useEffect, useRef, useState } from "react";
import Login from "../../pages/guest/LoginView";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMailBulk, faPhone } from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";
import Logo4sDigital from "../../assets/img/logo_no_text_white.png";

const FooterGuest = () => {
  const [isVisibleModalLogin, setIsVisibleModalLogin] = useState(false);

  const openModalLogin = () => {
    setIsVisibleModalLogin(true);
  };

  const cancelModalLogin = () => {
    setIsVisibleModalLogin(false);
  };

  const elementsToAnimate = useRef([]);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries, observer) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("visible");
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.1 }
    );

    elementsToAnimate.current.forEach((element) => {
      observer.observe(element);
    });

    return () => observer.disconnect();
  }, []);

  const menu = (
    <Menu>
      <Menu.Item key="1">
        <a
          href="https://zalo.me/g/qwbquv179"
          target="_blank"
          rel="noopener noreferrer"
          title="Nhóm quỹ 1"
        >
          <img src={iconZalo} alt="" />
          <span style={{ fontWeight: "500", marginLeft: 8 }}>Original</span>
        </a>
      </Menu.Item>
      <Menu.Item key="2">
        <a
          href="https://zalo.me/g/nrzrjt134"
          target="_blank"
          rel="noopener noreferrer"
          title="Nhóm quỹ 2"
        >
          <img src={iconZalo} alt="" />
          <span style={{ fontWeight: "500", marginLeft: 8 }}>Elite</span>
        </a>
      </Menu.Item>
    </Menu>
  );

  return (
    <footer>
      <Login isVisible={isVisibleModalLogin} onCancel={cancelModalLogin} />
      <section className="container-footer-guest">
        <Row>
          <Col
            xs={24}
            sm={8}
            className="col-footer animate-on-scroll"
            ref={(el) => (elementsToAnimate.current[0] = el)}
          >
            <div className="footer-column">
              <div className="title-footer-column-1">TCT FUND</div>
              <div className="subtitle-footer-column-2">
                TCT Fund là một quỹ mở được thành lập vào năm 2023, danh mục đầu
                tư tập trung vào thị trường tiền điện tử
              </div>
              <div className="icon-social-footer">
                <Row style={{ width: "80%" }}>
                  <Col xs={8} sm={8} md={8} lg={8} xl={8}>
                    <img src={iconFacebook} alt="Facebook" />
                    <br />
                    <span style={{ fontWeight: "500" }}>Fanpage</span>
                  </Col>{" "}
                  <Col xs={8} sm={8} md={8} lg={8} xl={8}>
                    <img src={iconTelegram} alt="" />
                    <br />
                    <span style={{ fontWeight: "500" }}>Telegram</span>
                  </Col>
                  <Col xs={8} sm={8} md={8} lg={8} xl={8}>
                    <Dropdown overlay={menu}>
                      <span
                        className="ant-dropdown-link"
                        onClick={(e) => e.preventDefault()}
                        style={{ cursor: "pointer" }}
                      >
                        <img src={iconZalo} alt="" />
                        <br />
                        <span style={{ fontWeight: "500" }}>Zalo</span>
                      </span>
                    </Dropdown>
                  </Col>
                </Row>
              </div>
            </div>
          </Col>
          <Col
            xs={24}
            sm={8}
            className="col-footer animate-on-scroll"
            ref={(el) => (elementsToAnimate.current[1] = el)}
          >
            <div
              style={{ display: "flex", justifyContent: "center" }}
              className="col-footer-box"
            >
              <div>
                <div className="title-footer-column-2">
                  LIÊN HỆ VỚI CHÚNG TÔI
                </div>
                <br />
                <div>
                  <FontAwesomeIcon icon={faPhone} style={{ marginRight: 5 }} />
                  +84 348 143 004
                </div>
                <br />
                <div>
                  {" "}
                  <FontAwesomeIcon
                    icon={faMailBulk}
                    style={{ marginRight: 5 }}
                  />
                  support-tctfund@gmail.com
                </div>
              </div>
            </div>
          </Col>
          <Col
            xs={24}
            sm={8}
            className="col-footer animate-on-scroll"
            ref={(el) => (elementsToAnimate.current[2] = el)}
          >
            <div
              style={{ display: "flex", justifyContent: "center" }}
              className="col-footer-box"
            >
              <div style={{ textAlign: "left" }}>
                <div className="title-footer-column-2">MENU</div> <br />
                <div>
                  {" "}
                  <Link to="/" className="link_footer_guest">
                    Trang chủ
                  </Link>
                </div>
                <br />
                <div>
                  {" "}
                  <Link to="/activity-reports" className="link_footer_guest">
                    Báo cáo hoạt động
                  </Link>
                </div>
                <br />
                <div>
                  <Link to="/recruitment" className="link_footer_guest">
                    Tuyển dụng
                  </Link>
                </div>
                <br />
                <div>
                  <Link to="/user-manual" className="link_footer_guest">
                    Hướng dẫn sử dụng
                  </Link>
                </div>
                <br />
                <div>
                  {" "}
                  <Link to="/policy-terms" className="link_footer_guest">
                    Chính sách & Điều khoản
                  </Link>
                </div>
              </div>
            </div>
          </Col>
        </Row>
        <div
          style={{
            justifyContent: "center",
            color: "gray",
            display: "flex",
            alignItems: "center",
            cursor: "pointer",
          }}
        >
          <img
            src={Logo4sDigital}
            alt=""
            style={{
              width: 45,
              height: 45,
              borderRadius: "50%",
              marginRight: 5,
            }}
          />{" "}
          <i
            className="copyright-guest-footer"
            style={{ color: "#003770", fontWeight: 500, fontSize: 13 }}
          >
            {" "}
            Copyright © 2024 - Developed by{" "}
            <Link
              to={`https://4sdigital.vn`}
              style={{ color: "#003770" }}
              target="_blank"
            >
              <span
                style={{ textDecoration: "underline" }}
                className="digital-services"
              >
                4S Digital Services
              </span>
            </Link>
          </i>
        </div>
      </section>
    </footer>
  );
};

export default FooterGuest;
