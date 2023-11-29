package com.vcc.agile.project.mgmt.PowerNPride.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private Integer numberOfPosts;
    private Integer numberOfComments;
    private Integer numberOfUsers;
    private Integer numberOfTopics;
}
