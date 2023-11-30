;FACTS ASSERTED BY GAME INPUT
(deftemplate CURRENTGHOST
	(slot tipo (type SYMBOL)))
(deftemplate NEWGHOST ; para el cambio de ghost
	(slot tipo (type SYMBOL)))

(deftemplate INDEX
	(slot name (type SYMBOL))  ; nombre del junction (no tiene por que ser junction)
	(slot index (type INTEGER)) ; el indice in game del junction
	(slot distance (type INTEGER)) ; la distancia que tenemos hasta ese index
)
	
(deftemplate MSPACMAN 
    (multislot INDEX) ; tendremos mindistancePPill, nextJunction, firstJunction, secondJunction, thirdJunction (opcional)
)	
(deftemplate GHOST
	(slot tipo (type SYMBOL))
	(slot alive (type SYMBOL)) ; Deberiamos poner esto o no poner el resto de slots??
	(slot edible (type SYMBOL))
	(slot nearestChasing (type SYMBOL))
	(slot nearestEdible (type SYMBOL))
	(multislot INDEX) ; tendremos mspacman, msNextJunction, msFirstJunction, msSecondJunction, msThirdJunction (opcional)
)

;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot ghostType (type SYMBOL)) ; Slot for the ghostType
	(slot junction (type INTEGER)) ; The junction where the ghost flanks mspacman
	(slot nearestEdible (type SYMBOL)) ; El ghost edible mas cercano en caso de que sea ProtectEdibleAction
	(slot nearestChasing (type SYMBOL)) ; El ghost chasing mas cercano en caso de que sea GoToChasingAction
) 


; RULES DE PRUEBA
(defrule delete-old-action
	?newGhost <- (NEWGHOST (tipo ?newType))
	?oldAction <- (ACTION (id ?id))
	=>
	(retract ?oldAction)
)

(defrule change-current-ghost
	?newGhost <- (NEWGHOST (tipo ?newType))
	?curGhost <- (CURRENTGHOST (tipo ?curType))
	=>
	(modify ?curGhost (tipo ?newType))
	(retract ?newGhost)
)

;(defrule delete-all-indexes-with-same-name
;	(GHOST (INDEX ?duck))
;	=>
;	(retract ?duck)
;)

;ACTION RULES
;(defrule BLINKYrunsAwayMSPACMANclosePPill
;	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30)) 
;	=>  
;	(assert 
;		(ACTION (id BLINKYrunsAway) (info "MSPacMan cerca PPill") (priority 50) 
;			(runawaystrategy AWAY)
;		)
;	)
;)

(defrule runsAway
	(CURRENTGHOST (tipo ?ghostType))
	(GHOST (tipo ?ghostType) (edible true)) 
	=>  
	(assert 
		(ACTION (id RunAway) (info "Comestible --> huir") (priority 30) 
			(ghostType ?ghostType)
		)
	)
)
(defrule chases
	(CURRENTGHOST (tipo ?ghostType))
	(GHOST (tipo ?ghostType) (edible false)) 
	=> 
	(assert 
		(ACTION (id ChaseMspacman) (info "No comestible --> perseguir")  (priority 10) 
			(ghostType ?ghostType)
		)
	)
	

)	
	
	