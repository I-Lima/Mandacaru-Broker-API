package com.mandacarubroker.controller;

import com.mandacarubroker.domain.treasury.GetResponseDataTransferObject;
import com.mandacarubroker.domain.treasury.RegisterDataTransferObject;
import com.mandacarubroker.domain.treasury.ResponseDataTransferObject;
import com.mandacarubroker.domain.treasury.Treasury;
import com.mandacarubroker.repository.TreasuryRepository;
import com.mandacarubroker.service.TreasuryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("checkstyle:MissingJavadocType")
@RestController
@RequestMapping("treasury")
public class TreasuryController {

  private final TreasuryService treasuryService;
  private final TreasuryRepository repository;

  public TreasuryController(
      TreasuryService treasuryService,
      TreasuryRepository treasuryRepository
  ) {
    this.treasuryService = treasuryService;
    this.repository = treasuryRepository;
  }

  @Operation(summary = "Get all assets", method = "POST")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "success"),
  })
  @GetMapping
  public ResponseEntity<GetResponseDataTransferObject> getAll() {
    List<Treasury> response = treasuryService.getAll();

    return ResponseEntity
        .ok()
        .body(new GetResponseDataTransferObject(
            true,
            null,
            response
        ));
  }

  @Operation(summary = "Get a specific asset", method = "POST")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "success"),
  })
  @GetMapping("/{id}")
  public ResponseEntity<ResponseDataTransferObject> get(@PathVariable String id) {
    var response = treasuryService.get(id);
    if (response.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(new ResponseDataTransferObject(
              false,
              "register not found",
              null
          ));
    }

    return ResponseEntity
        .ok()
        .body(new ResponseDataTransferObject(
            true,
            null,
            response
        ));
  }

  @Operation(summary = "Register a asset", method = "POST")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "success"),
  })
  @PostMapping("/register")
  public ResponseEntity<ResponseDataTransferObject> register(
      @RequestBody
      @Valid
      RegisterDataTransferObject data
  ) {
    var isRegistered = repository.getByName(data.name());
    if (isRegistered != null) {
      return ResponseEntity
          .badRequest()
          .body(new ResponseDataTransferObject(
              false,
              "Already registered",
              null
          ));
    }
    Treasury response = treasuryService.create(data);

    return ResponseEntity
        .ok()
        .body(new ResponseDataTransferObject(
            true,
            null,
            response
        ));
  }
  @Operation(summary = "Update a asset", method = "POST")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "success"),
  })
  @PutMapping("/{id}")
  public ResponseEntity<ResponseDataTransferObject> update(
      @PathVariable String id,
      @RequestBody @Valid RegisterDataTransferObject data
  ) {
    var response = treasuryService.update(id, data).orElse(null);
    if (response == null) {
      return ResponseEntity
          .status(500)
          .body(new ResponseDataTransferObject(
              false,
              "There was an error",
              null
          ));
    }

    return ResponseEntity
        .ok()
        .body(new ResponseDataTransferObject(
            true,
            null,
            response
        ));
  }
  @Operation(summary = "Delete a asset", method = "POST")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "success"),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDataTransferObject> delete(
      @PathVariable String id
  ) {
    treasuryService.delete(id);

    return ResponseEntity
        .ok()
        .body(new ResponseDataTransferObject(
            true,
            "successfully deleted",
            null
        ));
  }
}
