package shupship.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Province;
import shupship.domain.model.Users;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    private String empCode;
    private String name;
    private Long empId;

    public static UsersResponse leadModelToDto(Users users){
        UsersResponse model = new UsersResponse();
        model.setEmpCode(users.getEmployeeCode());
        model.setName(users.getFullName());
        model.setEmpId(users.getEmpSystemId());
        return model;
    }
}
