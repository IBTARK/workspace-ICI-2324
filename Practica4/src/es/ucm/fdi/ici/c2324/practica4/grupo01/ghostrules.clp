;FACTS ASSERTED BY GAME INPUT
(deftemplate CURRENTGHOST
	(slot tipo (type SYMBOL))
)
	
(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER))
    (slot firstJunction (type INTEGER))
    (slot secondJunction (type INTEGER))
    (slot thirdJunction (type INTEGER))
)	
(deftemplate GHOST
	(slot tipo (type SYMBOL))
	(slot alive (type SYMBOL)) ; Deberiamos poner esto o no poner el resto de slots??
	(slot edible (type SYMBOL))
	(slot nearestChasing (type SYMBOL))
	(slot nearestEdible (type SYMBOL))
	(slot distanceToMspacman (type INTEGER))
	(slot distanceToFirstJunction (type INTEGER))
	(slot distanceToSecondJunction (type INTEGER))
	(slot distanceToThirdJunction (type INTEGER))
)

;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot ghostType (type SYMBOL)) ; Slot for the ghostType
	(slot junction (type INTEGER)) ; The junction where the ghost flanks mspacman
	(slot nearestEdible (type SYMBOL)) ; El ghost edible mas cercano en caso de que sea ProtectEdibleAction
	(slot nearestChasing (type SYMBOL)) ; El ghost chasing mas cercano en caso de que sea GoToChasingAction
) 

;RULES 
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
	
	