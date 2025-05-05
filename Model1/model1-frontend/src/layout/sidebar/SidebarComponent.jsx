import React, { useEffect, useState } from "react";
import { Badge, Layout, Menu } from "antd";
import { Link, useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCoins,
  faUserCheck,
  faWallet,
  faUser,
  faHistory,
  faUserCog,
  faPiggyBank,
  faTags,
  faBullhorn,
  faClipboard,
  faBriefcase,
  faBell,
  faChartLine,
  faDollarSign,
  faBitcoinSign,
  faCogs,
  faCreditCard,
  faChartSimple,
  faHandHoldingUsd,
  faChevronCircleDown,
  faLaptop,
  faLaptopMedical,
  faMoneyCheckDollar,
  faSigning,
  faLandmarkFlag,
  faTeletype,
  faMoneyBillWave,
  faHandFist,
  faMoneyBills,
  faMoneyBillTransfer,
  faUserFriends,
  faCarTunnel,
} from "@fortawesome/free-solid-svg-icons";
import "./style-sidebar.css";
import { useAppDispatch } from "../../app/hook";
import { MenuUnfoldOutlined } from "@ant-design/icons";

const { Sider } = Layout;

const SidebarComponent = ({ collapsed, toggleCollapsed }) => {
  const location = useLocation();
  const dispatch = useAppDispatch();

  const [selectedKey, setSelectedKey] = useState("");

  useEffect(() => {
    setSelectedKey(location.pathname);
  }, [location]);

  return (
    <Sider
      trigger={null}
      collapsible
      collapsed={collapsed}
      theme={collapsed ? "dark" : "light"}
      className="sidebar"
      width={250}
      style={{
        overflow: "auto",
        position: "fixed",
        left: 0,
        top: 0,
        zIndex: 10000,
        color: "white",
        transition: "0.25s",
        backgroundColor: "#14213d",
      }}
    >
      <Menu
        theme={collapsed ? "dark" : "light"}
        mode="inline"
        style={{
          color: "white",
          paddingBottom: 80,
        }}
        selectedKeys={selectedKey}
      >
        {!collapsed && (
          <div style={{ textAlign: "center", marginBottom: 20 }}>
            <div
              style={{
                marginBottom: 30,
                textAlign: "center",
                fontSize: 25,
                fontWeight: "bold",
                marginTop: 10,
                textShadow: "3px 3px 4px #e38e02",
              }}
            >
              COSHINE 
            </div>
            <Link to={`/admin/fund-management`}>
              <img src="" alt="" width="40%" className="logo-tct-fund" />
            </Link>
          </div>
        )}
        {collapsed && (
          <div style={{ textAlign: "center", marginBottom: 20 }}>
            <div
              style={{
                fontSize: 22,
                fontWeight: "bold",
                marginTop: 5,
                marginBottom: 20,
                display: "flex",
                justifyContent: "center",
                cursor: "pointer",
              }}
              onClick={toggleCollapsed}
            >
              <MenuUnfoldOutlined />
            </div>{" "}
            <img src="" alt="" width="60%" />
          </div>
        )}{" "}
        <Menu.Item
          key="/brand-management"
          className="menu_custom"
          style={{ color: "white" }}
          icon={<FontAwesomeIcon icon={faCarTunnel} />}
        >
          <Link to="/brand-management">Báo cáo hoạt động</Link>
        </Menu.Item>
        <Menu.Item
          key="/user-management"
          className="menu_custom"
          style={{ color: "white" }}
          icon={<FontAwesomeIcon icon={faUser} />}
        >
          <Link to="/admin/user-management">Quản lý người dùng</Link>
        </Menu.Item>
      </Menu>
    </Sider>
  );
};

export default SidebarComponent;
