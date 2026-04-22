package br.com.guilda.controller;

import br.com.guilda.model.VisaoPainelTaticoMissao;
import br.com.guilda.service.ServicoPainelTaticoMissao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PortalPainelTaticoController {

    private final ServicoPainelTaticoMissao painelService;

    public PortalPainelTaticoController(ServicoPainelTaticoMissao service) {
        this.painelService = service;
    }

    @GetMapping("/missoes/top15dias")
    public ResponseEntity<List<VisaoPainelTaticoMissao>> buscarTop15Dias() {
        return ResponseEntity.ok(painelService.buscarTopMissoesUltimos15Dias());
    }
}

