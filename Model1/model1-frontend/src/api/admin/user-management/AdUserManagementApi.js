import { request } from "../../../helper/axios/request.helper";

const baseUrl = `/admin/user`;

export class AdUserManagementApi {
  static getList = (filter) => {
    return request({
      method: "GET",
      url: `${baseUrl}`,
      params: filter,
    });
  };

  static create = (data) => {
    return request({
      method: "POST",
      url: `${baseUrl}/create`,
      data: data,
    });
  };

  static delete = (id) => {
    return request({
      method: "DELETE",
      url: `${baseUrl}/delete/${id}`,
    });
  };

  static update = (data) => {
    return request({
      method: "PUT",
      url: `${baseUrl}/update`,
      data: data,
    });
  };

  static detail = (id) => {
    return request({
      method: "GET",
      url: `${baseUrl}/detail/${id}`,
    });
  };
}
