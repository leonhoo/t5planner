package tech.hooo.tools.t5planner.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {
    private Integer pageSize;
    private Integer pageNo;
    private String searchText;
}
