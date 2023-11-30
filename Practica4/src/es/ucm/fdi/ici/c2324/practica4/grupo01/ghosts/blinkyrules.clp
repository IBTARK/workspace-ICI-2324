;FACTS ASSERTED BY GAME INPUT
(deftemplate CURRENTGHOST
	(slot tipo (type SYMBOL))
(deftemplate BLINKY
	(slot alive (type SYMBOL)) ; Deberiamos poner esto o no poner el resto de slots??
	(slot edible (type SYMBOL))
	(slot nearestChasing (type SYMBOL))
	(slot nearestEdible (type SYMBOL))
	(slot distanceToMspacman (type INTEGER))	
)
	
(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot nearestChasing (type SYMBOL))
	(slot nearestEdible (type SYMBOL))
	(slot distanceToMspacman (type INTEGER))
)
	
(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot nearestChasing (type SYMBOL))
	(slot nearestEdible (type SYMBOL))
	(slot distanceToMspacman (type INTEGER))
)

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot nearestChasing (type SYMBOL))
	(slot nearestEdible (type SYMBOL))
	(slot distanceToMspacman (type INTEGER))
)
	
(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER)) )
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot runawaystrategy (type SYMBOL)) ; Extra slot for the runaway action
	(slot nearestChasing (type SYMBOL)) ; El ghost chasing mas cercano en caso de que runawaystrategy sea GOTO_CHASING
	(slot chasestrategy (type SYMBOL)) ; Extra slot for the chase action
	(slot nearestEdible (type SYMBOL)) ; El ghost edible mas cercano en caso de que chasestrategy sea PROTECT_EDIBLE
) 

;RULES 
(defrule BLINKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30)) 
	=>  
	(assert 
		(ACTION (id BLINKYrunsAway) (info "MSPacMan cerca PPill") (priority 50) 
			(runawaystrategy AWAY)
		)
	)
)

(defrule BLINKYrunsAway
	(BLINKY (edible true)) 
	=>  
	(assert 
		(ACTION (id BLINKYrunsAway) (info "Comestible --> huir") (priority 30) 
			(runawaystrategy CORNER)
		)
	)
)
(defrule BLINKYchases
	(BLINKY (edible false)) 
	=> 
	(assert (ACTION (id BLINKYchases) (info "No comestible --> perseguir")  (priority 10) ))
)	
	
	