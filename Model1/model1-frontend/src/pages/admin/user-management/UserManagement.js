import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import TitleComponent from "../../../component/common/form/TitleComponent";
import {
  faEdit,
  faFilter,
  faPlus,
  faRefresh,
  faSearch,
  faTable,
  faTrashAlt,
} from "@fortawesome/free-solid-svg-icons";
import ContainerComponent from "../../../component/common/form/ContainerComponent";
import {
  Row,
  Col,
  Input,
  Button,
  Table,
  message,
  Modal,
  Popconfirm,
  Tooltip,
} from "antd";
import { useEffect, useState } from "react";
import { rowClassName } from "../../../helper/common/utils";
import { dispatch } from "../../../app/store";
import { SetLoading } from "../../../app/reducer/common/LoadingSlice.reducer";
import ModalAddUser from "./modal-add-user/ModalAddUser";
import ModalUpdateUser from "./modal-update-user/ModalUpdateUser";
import "./style-user-management.css";
import { AdUserManagementApi } from "../../../api/admin/user-management/AdUserManagementApi";

const UserManagement = () => {
  const [listUsers, setListUsers] = useState([]);
  const [searchName, setSearchName] = useState("");
  const [searchEmail, setSearchEmail] = useState("");
  const [current, setCurrent] = useState(1);
  const [size, setSize] = useState("10");

  // USE EFFECT
  useEffect(() => {
    document.title = "Quản lý người dùng";
    loadData();
  }, [current, size]);

  // LOAD DATA
  
  const loadData = () => {
    dispatch(SetLoading(true));
    AdUserManagementApi.getList().then((res) => {
      setListUsers(res.data);
      dispatch(SetLoading(false));
    });
  };

  // CLEAR
  const clearFormSearch = () => {
    setSearchName("");
    setSearchEmail("");
  };

  // COLUMNS
  const columns = [
    {
      title: "STT",
      dataIndex: "rowNum",
      key: "rowNum",
      width: "6%",
      sorter: (a, b) => a.rowNum - b.rowNum,
    },
    {
      title: "Họ tên",
      dataIndex: "name",
      key: "name",
      render: (text, record) => (
        <span style={{ fontWeight: 500 }}>{record.name}</span>
      ),
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      render: (text, record) => <span>{record.email}</span>,
    },
    {
      title: "Vai trò",
      dataIndex: "role",
      key: "role",
      render: (text, record) => (
        <span>{record.role === 1 ? "Admin" : "User"}</span>
      ),
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      width: "15%",
      render: (text, record) => (
        <Button
          style={record.status === 1 ? { opacity: 0.7 } : { color: "green" }}
          onClick={() => toggleUserStatus(record)}
        >
          {record.status ? "Khóa" : "Mở khóa"}
        </Button>
      ),
    },
    {
      title: "Hành động",
      dataIndex: "actions",
      key: "actions",
      width: "20%",
      render: (text, record) => (
        <div>
          <FontAwesomeIcon
            icon={faEdit}
            style={{
              fontSize: 17,
              color: "#fca311",
              cursor: "pointer",
              marginRight: 10,
            }}
            onClick={() => openModalUpdateUser(record.id)}
          />
          <Popconfirm
            title="Xác nhận"
            description={"Bạn có chắc chắn muốn xóa?"}
            onConfirm={() => deleteUser(record.id)}
            okText="Xóa"
            cancelText="Hủy"
            placement="topLeft"
          >
            <Tooltip title="Xóa">
              <FontAwesomeIcon
                icon={faTrashAlt}
                style={{ cursor: "pointer", fontSize: 16, color: "#fca311" }}
              />
            </Tooltip>
          </Popconfirm>
        </div>
      ),
    },
  ];

  // TOGGLE STATUS
  const toggleUserStatus = (record) => {
    const newStatus = record.status === 1 ? 0 : 1;
    const request = {
      id: record.id,
      name: record.name,
      email: record.email,
      role: record.role,
      status: newStatus,
    };
    // UserManagementApi.update(request).then(
    //   (res) => {
    //     loadData();
    //     message.success("Cập nhật trạng thái thành công");
    //   },
    //   (err) => {
    //     message.error("Cập nhật trạng thái thất bại");
    //   }
    // );
  };

  // DELETE
  const deleteUser = (id) => {
    dispatch(SetLoading(true));
    // UserManagementApi.delete(id).then(
    //   (res) => {
    //     dispatch(SetLoading(false));
    //     message.success("Xóa người dùng thành công");
    //     loadData();
    //   },
    //   (err) => {
    //     dispatch(SetLoading(false));
    //     message.error("Xóa người dùng thất bại");
    //   }
    // );
  };

  // CREATE
  const [visibleModalAdd, setVisibleModalAdd] = useState(false);
  const [idSelected, setIdSelected] = useState(null);

  const openModalAddUser = () => {
    setVisibleModalAdd(true);
    setIdSelected(null);
  };

  const cancelModalAddUser = () => {
    setVisibleModalAdd(false);
    setIdSelected(null);
  };

  // UPDATE
  const [visibleModalUpdate, setVisibleModalUpdate] = useState(false);

  const openModalUpdateUser = (id) => {
    setIdSelected(id);
    setVisibleModalUpdate(true);
  };

  const cancelModalUpdateUser = () => {
    setIdSelected(null);
    setVisibleModalUpdate(false);
  };

  // SEARCH
  const handleSearch = () => {
    // UserManagementApi.search({ name: searchName, email: searchEmail }).then(
    //   (res) => {
    //     setListUsers(res.data.data);
    //   }
    // );
  };

  // RETURN
  return (
    <div>
      <TitleComponent>
        <FontAwesomeIcon icon={faTable} style={{ marginRight: 10 }} />
        Quản lý người dùng
      </TitleComponent>
      {/* <ModalAddUser
        visible={visibleModalAdd}
        handleCancel={cancelModalAddUser}
        fetchData={loadData}
        id={idSelected}
      /> */}
      {/* <ModalUpdateUser
        visible={visibleModalUpdate}
        handleCancel={cancelModalUpdateUser}
        fetchData={loadData}
        id={idSelected}
      /> */}
      <div style={{ marginTop: 30 }}>
        <ContainerComponent>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <div style={{ fontSize: 18, fontWeight: 500 }}>
              <FontAwesomeIcon icon={faTable} style={{ marginRight: 5 }} />
              Danh sách người dùng
            </div>
            <div style={{ fontSize: 18, fontWeight: 500 }}>
              <Button className="btn-common" onClick={openModalAddUser}>
                <FontAwesomeIcon icon={faPlus} style={{ marginRight: 5 }} />
                Thêm người dùng
              </Button>
            </div>
          </div>
          <div style={{ marginTop: 20 }}>
            <Row gutter={[16, 16]}>
              <Col span={8}>
                <Input
                  placeholder="Tìm kiếm theo tên"
                  value={searchName}
                  onChange={(e) => setSearchName(e.target.value)}
                />
              </Col>
              <Col span={8}>
                <Input
                  placeholder="Tìm kiếm theo email"
                  value={searchEmail}
                  onChange={(e) => setSearchEmail(e.target.value)}
                />
              </Col>
              <Col span={8}>
                <Button
                  className="btn-common"
                  onClick={handleSearch}
                  style={{ marginRight: 10 }}
                >
                  <FontAwesomeIcon icon={faSearch} style={{ marginRight: 5 }} />
                  Tìm kiếm
                </Button>
                <Button onClick={clearFormSearch}>
                  <FontAwesomeIcon
                    icon={faRefresh}
                    style={{ marginRight: 5 }}
                  />
                  Xóa bộ lọc
                </Button>
              </Col>
            </Row>
          </div>
          <div style={{ marginTop: 20 }}>
            <Table
              columns={columns}
              dataSource={listUsers}
              rowClassName={rowClassName}
              rowKey="rowNum"
              pagination={{
                current: current,
                pageSize: size,
                onChange: (page, pageSize) => {
                  setCurrent(page);
                  setSize(pageSize);
                },
              }}
            />
          </div>
        </ContainerComponent>
      </div>
    </div>
  );
};

export default UserManagement;
