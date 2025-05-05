import { request } from "../../../util/axios/request.helper";

const baseUrl = `/sinh-vien-management`;

export class SinhVienManagementApi {
  static getListSinhVien = (filter) => {
    return request({
      method: "GET",
      url: `${baseUrl}/list`,
      params: filter,
    });
  };

  static detailSinhVien = (id) => {
    return request({
      method: "GET",
      url: `${baseUrl}/detail/${id}`,
    });
  };

  static createSinhVien = (data) => {
    return request({
      method: "POST",
      url: `${baseUrl}/create`,
      data: data,
    });
  };

  static updateSinhVien = (data) => {
    return request({
      method: "PUT",
      url: `${baseUrl}/update`,
      data: data,
    });
  };

  static deleteSinhVien = (id) => {
    return request({
      method: "DELETE",
      url: `${baseUrl}/delete/${id}`,
    });
  };
}
