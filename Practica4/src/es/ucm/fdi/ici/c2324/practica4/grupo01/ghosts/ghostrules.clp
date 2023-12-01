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
	(slot nearestEdible (type SYMBOL)) ; El ghost edible mas cercano en caso de que sea ProtectEdibleAction
	(slot nearestChasing (type SYMBOL)) ; El ghost chasing mas cercano en caso de que sea GoToChasingAction
) 


; FUNCTIONS DE UTILIDAD
(deffunction calcular-closest-distance-index-menor-que-mspacman (?ghostType ?lvl)
	(INDEX (owner MSPACMAN) (lvl ?lvl) (index ?index) (distance ?idms))
	(INDEX (owner ?ghostType) (lvl ?lvl) (index ?index) (distance ?id1&:(<= ?id1 ?idms)))
	(not (INDEX (owner ?ghostType) (distance ?id2&:(< ?id2 ?id1))))
	(return ?index)
)

(deffunction elegir-index-o-previous (?ghostType ?lvl ?index)
	(INDEX (owner ?ghostType) (lvl ?lvl) (index ?index) (previousIndex ?pri) (distance ?dist))
	(if (= ?dist 0) then
		(return ?pri)
	)
	(if (> ?dist 0) then
		(return ?index)
	)
)

(deffunction devolver-index-objetivo (?ghostType ?lvl)
	(bind ?index (calcular-closest-distance-index-menor-que-mspacman ?ghostType ?lvl))
	(test (neq ?index nil))
	(bind ?indexObjetivo (elegir-index-o-previous ?ghostType ?lvl ?index))
	(return ?indexObjetivo)
)

; EL USO DE LA FUNCION ANTERIOR SERIA DE LA SIGUIENTE MANERA
; (bind ?objetivo (devolver-index-objetivo ?ghostType ?lvl))


;(deffunction calcular-closest-distance-index (?ghostType ?lvl ?returnIndex)
;	(INDEX (owner ?ghostType) (index ?index) (distance ?id))
;	(not (INDEX (owner ?ghostType) (distance ?id2&:(< ?id2 ?id1))))
;	(bind ?returnIndex ?index)
;)
	

; REGLAS DE UTILIDAD QUE NO ASERTAN ACTIONS

(defrule calcular-nearest-ghost
	(NEWGHOST )
	(GHOST (tipo ?ghostType) (edible false) (mspacman ?d1)) 
	(not (GHOST (edible false) (mspacman ?d2&:(< ?d2 ?d1))))
	?curNearest <- (NEARESTGHOST )
	=>
	(modify ?curNearest (tipo ?ghostType))
)
	

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

(defrule flank-lvl2-junctions
	(not (ACTION))
	(not (NEWGHOST))
	(CURRENTGHOST (tipo ?ghostType))
	; SI !NO! SOMOS EL GHOST MAS CERCANO A MSPACMAN
	(GHOST (tipo ?ghostType) (edible false) (mspacman ?d1))
	(GHOST (tipo ?another&:(neq ?another ?ghostType)) (edible false) (mspacman ?d2&:(< ?d2 ?d1)))
	; CALCULAMOS EL INDEX (owner ?ghostType) con distancia minima.
	(INDEX (owner MSPACMAN) (index ?index) (distance ?idms))
	(INDEX (owner ?ghostType) (index ?index) (distance ?id1&:(> ?id1 0)&:(<= ?id1 ?idms)))
	(not (INDEX (owner ?ghostType) (distance ?id2&:(< ?id2 ?id1))))
	=>
	(assert
		(ACTION (id FlankMspacman) (info "No comestible --> Flankear")  (priority 30) 
			(ghostType ?ghostType)
			(junction ?index)
		)
	)
)

(defrule flank-lvl3-junctions
	(not (ACTION))
	(not (NEWGHOST))
	(CURRENTGHOST (tipo ?ghostType))
	; SI !NO! SOMOS EL GHOST MAS CERCANO A MSPACMAN
	(GHOST (tipo ?ghostType) (edible false) (mspacman ?d1))
	(GHOST (tipo ?another&:(neq ?another ?ghostType)) (edible false) (mspacman ?d2&:(< ?d2 ?d1)))
	; CALCULAMOS EL INDEX (owner ?ghostType) con distancia minima.
	(INDEX (owner MSPACMAN) (lvl 3) (index ?index) (distance ?idms))
	(INDEX (owner ?ghostType) (lvl 3) (index ?index) (distance ?id1&:(<= ?id1 ?idms)))
	(not (INDEX (owner ?ghostType) (lvl 3) (distance ?id2&:(< ?id2 ?id1))))
	=>
	(assert
		(ACTION (id FlankMspacman) (info "No comestible --> Flankear")  (priority 20) 
			(ghostType ?ghostType)
			(junction ?index)
		)
	)
)


(defrule ultimo-fantasma-no-tiene-index-para-asignar
	(not (INDEX))
	(not (NEWGHOST))
	(CURRENTGHOST (tipo ?ghostType))
	(GHOST (tipo ?ghostType) (edible false))
	=>
	(assert
		(ACTION (id ChaseMspacman) (info "No tenemos junction --> Chase") (priority 10)
			(ghostType ?ghostType)
		)
	)
)

; ULTIMA REGLA
(defrule delete-all-indexes-with-same-index-if-action-has-junction
	(not (NEWGHOST))
	(ACTION (junction ?junction&:(neq ?junction nil)) (priority ?p1))
	(not (ACTION (priority ?p2&:(> ?p2 ?p1))))
	?duck <- (INDEX (index ?junction))
	=>
	(retract ?duck)
)
	