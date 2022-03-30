package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.domian.CV
import ru.ivan.students.dto.request.CVRequest
import ru.ivan.students.dto.response.CVResponse
import ru.ivan.students.service.CVService
import java.security.Principal

@RestController
@RequestMapping("/api/cv")
class CVController {
    @Autowired
    private lateinit var cvService: CVService

    @GetMapping("/all")
    fun showAll(): List<CVResponse> = cvService.showAll()

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "security_auth")
    fun addCV(principal: Principal, @RequestBody CVRequest: CVRequest): ResponseEntity<CVResponse> {
        return ResponseEntity.ok(cvService.saveCV(CVRequest, principal.name))
    }
}