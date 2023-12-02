;FACTS ASSERTED BY GAME INPUT
(deftemplate CURRENTGHOST
	(slot tipo (type SYMBOL)))
(deftemplate NEWGHOST ; para el cambio de ghost
	(slot tipo (type SYMBOL)))
(deftemplate NEARESTGHOST
	(slot tipo (type SYMBOL)))

(deftemplate INDEX
	(slot owner (type SYMBOL))  ; owner del INDEX: MSPACMAN, BLINKY, PINKY, INKY, SUE
	(slot lvl (type INTEGER)) ; nivel
	(slot index (type INTEGER)) ; el indice in game del junction
	(slot previousIndex (type INTEGER)) ; el indice o junction de donde viene
	(slot distance (type INTEGER)) ; la distancia que tenemos hasta ese index
)
	
(deftemplate MSPACMAN 
	(slot ppill (type INTEGER))
)

(deftemplate GHOST
	(slot tipo (type SYMBOL))
	(slot alive (type SYMBOL)) ; Deberiamos poner esto o no poner el resto de slots??
	(slot edible (type SYMBOL))
	(slot nearestChasing (type SYMBOL))
	(slot nearestEdible (type SYMBOL))
	(slot mspacman (type INTEGER))
)

;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot ghostType (type SYMBOL)) ; Slot for the ghostType
	(slot junction (type INTEGER)) ; The junction where the ghost goes to flank mspacman
	(slot borrar (type INTEGER)) ; informacion extra para la mejor sincronizacion de los fantasmas
	(slot lvl (type INTEGER)) 	 ; informacion extra para la mejor sincronizacion de los fantasmas
	(slot nearestEdible (type SYMBOL)) ; El ghost edible mas cercano en caso de que sea ProtectEdibleAction
	(slot nearestChasing (type SYMBOL)) ; El ghost chasing mas cercano en caso de que sea GoToChasingAction
)



; REGLAS DE UTILIDAD QUE NO ASERTAN ACTIONS

; En el caso de que nearestghost sea null lo calculamos
;(defrule calcular-nearest-ghost
;	(NEARESTGHOST (tipo nil))
;	(GHOST (tipo ?ghostType) (edible false) (mspacman ?d1)) 
;	(not (GHOST (edible false) (mspacman ?d2&:(< ?d2 ?d1))))
;	?curNearest <- (NEARESTGHOST )
;	=>
;	(modify ?curNearest (tipo ?ghostType))
;)
	

; ANTES DE HACER EL CAMBIO DE GHOST, ELIMINAMOS LOS ACTIONS ASERTADOS EN EL ANTERIOR TURNO.
(defrule delete-old-action
	(NEWGHOST (tipo ?newType))
	?oldAction <- (ACTION (id ?id))
	=>
	(retract ?oldAction)
)

; CAMBIAMOS DE CURRENTGHOST
(defrule change-current-ghost
	?newGhost <- (NEWGHOST (tipo ?newType))
	?curGhost <- (CURRENTGHOST (tipo ?curType))
	=>
	(modify ?curGhost (tipo ?newType))
	(retract ?newGhost)
)


;RULES QUE ASERTAN ACTION


(defrule runsAway
	(not (NEWGHOST))
	(CURRENTGHOST (tipo ?ghostType))
	(GHOST (tipo ?ghostType) (edible true) (nearestChasing null))
	=>  
	(assert 
		(ACTION (id RunAway) (info "Comestible --> huir") (priority 30) 
			(ghostType ?ghostType)
		)
	)
)

(defrule MSPACMANclosePPill-runaway
	(not (NEWGHOST))
	(CURRENTGHOST (tipo ?ghostType))
	(GHOST (tipo ?ghostType) (edible false))
	(MSPACMAN (ppill ?d)) (test (<= ?d 30)) 
	=>  
	(assert 
		(ACTION (id RunAway) (info "MSPacMan cerca PPill") (priority 50) 
			(ghostType ?ghostType)
		)
	)
)

(defrule goToChasing
	(not (NEWGHOST))
	(CURRENTGHOST (tipo ?ghostType))
	(GHOST (tipo ?ghostType) (edible true) (nearestChasing ?nearestChasing&:(neq ?nearestChasing null)))
	=>  
	(assert 
		(ACTION (id GoToChasing) (info "Comestible --> huir") (priority 40) 
			(ghostType ?ghostType)
			(nearestChasing ?nearestChasing)
		)
	)
)




(defrule chases
	(not (NEWGHOST))
	(CURRENTGHOST (tipo ?ghostType))
	; SI SOMOS EL GHOST MAS CERCANO A MSPACMAN
	(GHOST (tipo ?ghostType) (edible false) (mspacman ?d1)) 
	(not (GHOST (edible false) (mspacman ?d2&:(< ?d2 ?d1)))) 
	=> 
	(assert 
		(ACTION (id ChaseMspacman) (info "No comestible --> perseguir")  (priority 50) 
			(ghostType ?ghostType)
		)
	)
)	

(defrule ir-a-junction-lvl1
	(not (NEWGHOST))
	(not (ACTION))
	(CURRENTGHOST (tipo ?ghostType))
	; SI !NO! SOMOS EL GHOST MAS CERCANO A MSPACMAN
	(GHOST (tipo ?ghostType) (edible false) (mspacman ?d1))
	(GHOST (tipo ?another&:(neq ?another ?ghostType)) (edible false) (mspacman ?d2&:(< ?d2 ?d1)))
	; CALCULAMOS EL INDEX (owner ?ghostType) con distancia minima.
	(INDEX (owner MSPACMAN) (lvl 1) (index ?index) (distance ?idms))
	(INDEX (owner ?ghostType) (lvl 1) (index ?index) (previousIndex ?pri) (distance ?id1&:(<= ?id1 ?idms)))
	=>
	(if (> ?id1 0)
		then
		(assert
			(ACTION (id FlankMspacman) (info "No comestible --> Flankear junction")  (priority 30) 
				(ghostType ?ghostType)
				(junction ?index)
				(lvl 1)
			)
		)
		else
		(assert
			(ACTION (id ChaseMspacman) (info "No comestible --> Flankear junction previo mspacman")  (priority 30) 
				(ghostType ?ghostType)
			)
		)
	)
)

(defrule flank-lvl2-junctions
	(not (NEWGHOST))
	(not (ACTION))
	(CURRENTGHOST (tipo ?ghostType))
	; SI !NO! SOMOS EL GHOST MAS CERCANO A MSPACMAN
	(GHOST (tipo ?ghostType) (edible false) (mspacman ?d1))
	(GHOST (tipo ?another&:(neq ?another ?ghostType)) (edible false) (mspacman ?d2&:(< ?d2 ?d1)))
	; CALCULAMOS EL INDEX (owner ?ghostType) con distancia minima.
	;(INDEX (owner MSPACMAN) (lvl 2) (index ?index) (distance ?idms))
	;(INDEX (owner ?ghostType) (lvl 2) (index ?index) (previousIndex ?pri) (distance ?id1&:(<= ?id1 ?idms)))
	(INDEX (owner ?ghostType) (lvl 2) (index ?index) (previousIndex ?pri) (distance ?id1))
	(not (INDEX (owner ?ghostType) (lvl 2) (distance ?id2&:(< ?id2 ?id1))))
	=>
	(if (> ?id1 0) then
		(assert
			(ACTION (id FlankMspacman) (info "No comestible --> Flankear junction lvl2")  (priority 20) 
				(ghostType ?ghostType)
				(junction ?index)
				(borrar ?index)
				(lvl 2)
			)
		)
	else
		(assert
			(ACTION (id FlankMspacman) (info "No comestible --> Flankear junction previo lvl1")  (priority 20) 
				(ghostType ?ghostType)
				(junction ?pri)
				(borrar ?index)
				(lvl 2)
			)
		)
	)
)


(defrule flank-lvl3-junctions
	(not (NEWGHOST))
	(CURRENTGHOST (tipo ?ghostType))
	; SI !NO! SOMOS EL GHOST MAS CERCANO A MSPACMAN
	(GHOST (tipo ?ghostType) (edible false) (mspacman ?d1))
	(GHOST (tipo ?another&:(neq ?another ?ghostType)) (edible false) (mspacman ?d2&:(< ?d2 ?d1)))
	; CALCULAMOS EL INDEX (owner ?ghostType) con distancia minima.
	(INDEX (owner ?ghostType) (lvl 3) (index ?index) (previousIndex ?pri) (distance ?id1))
	(not (INDEX (owner ?ghostType) (lvl 3) (distance ?id2&:(< ?id2 ?id1))))
	=>
	(if (> ?id1 0)
		then
		(assert
			(ACTION (id FlankMspacman) (info "No comestible --> Flankear junction lvl3")  (priority 10) 
				(ghostType ?ghostType)
				(junction ?index)
				(borrar ?index)
				(lvl 3)
			)
		)
		else
		(assert
			(ACTION (id FlankMspacman) (info "No comestible --> Flankear junction previo lvl2")  (priority 10) 
				(ghostType ?ghostType)
				(junction ?pri)
				(borrar ?index)
				(lvl 3)
			)
		)
	)
)


;(defrule ultimo-fantasma-no-tiene-index-para-asignar
;	(not (INDEX))
;	(not (NEWGHOST))
;	(CURRENTGHOST (tipo ?ghostType))
;	(GHOST (tipo ?ghostType) (edible false))
;	=>
;	(assert
;		(ACTION (id ChaseMspacman) (info "No tenemos junction --> Chase") (priority 10)
;			(ghostType ?ghostType)
;		)
;	)
;)


; estas ultimas reglas se encargan de eliminar los indices cuyo index ?index y previousIndex ?index y el lvl es acorde
(defrule retract-previous-indices
	(not (NEWGHOST))
	(ACTION (borrar ?index&:(neq ?index nil)) (lvl ?lvl) (priority ?p1))
	(not (ACTION (priority ?p2&:(> ?p2 ?p1))))
	?duck <- (INDEX (previousIndex ?index) (lvl ?nextLvl&:(> ?nextLvl ?lvl)))
	=>
	(retract ?duck)
)

(defrule retract-indices
	(not (NEWGHOST))
	(ACTION (borrar ?index&:(neq ?index nil)) (lvl ?lvl) (priority ?p1))
	(not (ACTION (priority ?p2&:(> ?p2 ?p1))))
	?duck <- (INDEX (index ?index) (lvl ?lvl))
	=>
	(retract ?duck)
)
	
	