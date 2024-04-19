package fr.norsys.apidocument.document.controllers;

import fr.norsys.apidocument.document.models.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareRequest {
    private UUID documentUUID;
    private String email;
    private PermissionType permission;
}
