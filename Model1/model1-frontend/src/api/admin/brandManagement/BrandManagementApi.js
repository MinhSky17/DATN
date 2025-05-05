import { request } from "../../../util/axios/request.helper";

//const baseUrl = `/admin/brand-management`;
const baseUrl = `http://localhost:8080/coshine/admin/brand-management`;

export class BrandManagementApi {
  static create = (data) => {
    return request({
      method: "POST",
      url: `${baseUrl}`,
      data: data,
    });
  };
}
