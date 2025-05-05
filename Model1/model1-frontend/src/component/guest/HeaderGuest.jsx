import { Button, Menu, Popover } from "antd";
import "./style-header.css";

import { useEffect, useRef, useState } from "react";
import Login from "../../pages/guest/LoginView";
import Register from "../../pages/guest/RegisterView";
import LogoTctFund from "../../assets/img/logo_tct_fund.png";
import { useLocation } from "react-router";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons";

const menuItems = [
  { key: "1", text: "Trang chủ", link: "/" },
  { key: "2", text: "Báo cáo hoạt động", link: "/activity-reports" },
  { key: "3", text: "Tuyển dụng", link: "/recruitment" },
  { key: "4", text: "Hướng dẫn sử dụng", link: "/user-manual" },
  { key: "5", text: "Chính sách & Điều khoản", link: "/policy-terms" },
];

const HeaderGuest = () => {
  const [isVisibleModalLogin, setIsVisibleModalLogin] = useState(false);

  const openModalLogin = () => {
    setIsVisibleModalLogin(true);
  };

  const cancelModalLogin = () => {
    setIsVisibleModalLogin(false);
  };

  const [isVisibleModalRegister, setIsVisibleModalRegister] = useState(false);

  const openModalRegister = () => {
    setIsVisibleModalRegister(true);
  };

  const cancelModalRegister = () => {
    setIsVisibleModalRegister(false);
  };

  const elementsToAnimate = useRef([]);

  const location = useLocation();

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

  const listUrlHeaderV2 = ["/recruitment"];

  const menu = (
    <Menu>
      {menuItems.map((item) => (
        <Menu.Item key={item.key}>
          <Link to={item.link}>{item.text}</Link>
        </Menu.Item>
      ))}
    </Menu>
  );

  const [visible, setVisible] = useState(false);

  const handleClick = () => {
    setVisible(!visible);
  };

  return (
    <header>
      <Login isVisible={isVisibleModalLogin} onCancel={cancelModalLogin} />
      <Register
        isVisible={isVisibleModalRegister}
        onCancel={cancelModalRegister}
      />
      <div className="menu-fixed" onClick={handleClick}>
        <Popover
          content={menu}
          trigger="click"
          overlayClassName="popover-menu"
          placement="leftTop"
        >
          <FontAwesomeIcon icon={faBars} className="menu-icon" />
        </Popover>
      </div>
      <section
        className={
          location.pathname.includes(listUrlHeaderV2)
            ? "container-header-guest-v2"
            : "container-header-guest"
        }
      >
        <div className="header-left">
          <div className="section_title_header_guest">
            <Link to={`/`}>
              {" "}
              <img src={LogoTctFund} alt="" className="logo_tct_fund_guest" />
            </Link>
            <h2 className="title_header_guest">COSHINE</h2>
          </div>
        </div>
        <div
          className={
            location.pathname.includes(listUrlHeaderV2)
              ? "header-menu-v2"
              : "header-menu"
          }
        >
          <nav>
            <ul>
              {menuItems.map((item) => (
                <li key={item.key}>
                  <Link
                    to={item.link}
                    className={location.pathname === item.link ? "active" : ""}
                  >
                    {item.text}
                  </Link>
                </li>
              ))}
            </ul>
          </nav>
        </div>
        <div className="header-right">
          <button
            className={
              location.pathname.includes(listUrlHeaderV2)
                ? "button_header_guest_v2"
                : "button_header_guest"
            }
            onClick={openModalLogin}
          >
            <span className="text_button_header_guest">Đăng nhập</span>
          </button>
          <button
            className={
              location.pathname.includes(listUrlHeaderV2)
                ? "button_header_guest_v2"
                : "button_header_guest"
            }
            onClick={openModalRegister}
          >
            <span className="text_button_header_guest">Đăng ký</span>
          </button>
        </div>
      </section>
    </header>
  );
};

export default HeaderGuest;
