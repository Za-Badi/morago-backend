package com.habsida.morago.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.habsida.morago.model.dto.AppVersionDTO;
import com.habsida.morago.model.enums.EPlatform;
import com.habsida.morago.serviceImpl.AppVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppVersionQueryResolver implements GraphQLQueryResolver {
    private final AppVersionService appVersionService;

    public AppVersionDTO getAppVersionByPlatform(EPlatform platform) {
        return appVersionService.getAppVersionByPlatform(platform);
    }

    public List<AppVersionDTO> getAllAppVersions() {
        return appVersionService.getAll();
    }
}
