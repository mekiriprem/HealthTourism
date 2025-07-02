package hospital.tourism.Dto;

import lombok.Data;

@Data
public class DoctorsDTO {

    private Long id;
    private String name;
    private String email;
    private double rating;
    private String description;
    private String department;
    private String profilepic;
    private String status;
    private Integer hospitalId;
    private String hospitalName;
    private String city;
    private String state;
    private String country;// optional, useful for display
}
