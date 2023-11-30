; example clp file for debugging
(deftemplate CURRENTGHOST
	(slot tipo (type SYMBOL))
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

(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot edibleTime (type NUMBER))
	(slot position (type SYMBOL))
)

(deftemplate ACTION
	(slot id) 
	(slot info (default "")) 
	(slot ghost (type SYMBOL)) 
	(slot ghostType (type SYMBOL)) 
	(slot edible (type SYMBOL)) 
) 


(deffacts hechos-asertados
	(BLINKY (edible false) (edibleTime 2) (position UP))
	(GHOST (tipo BLINKY) (edible true))
	(CURRENTGHOST (tipo BLINKY))
)

(defrule regla1
	(BLINKY (edible ?e) (edibleTime ?t))
	(test (> ?t 5))
	=>
	(assert (ACTION (id correr) (ghost BLINKY) (edible ?e)))
)


(defrule runsAway
	(CURRENTGHOST (tipo ?ghostType))
	(GHOST (tipo ?ghostType) (edible true)) 
	=>  
	(assert 
		(ACTION (id RunAway) (info "?ghostType Comestible --> huir")  
			(ghostType ?ghostType)
		)
	)
)
(defrule chases
	(CURRENTGHOST (tipo ?ghostType))
	(GHOST (tipo ?ghostType) (edible false)) 
	=> 
	(assert 
		(ACTION (id ChaseMspacman) (info "?ghostType No comestible --> perseguir") 
			(ghostType ?ghostType)
		)
	)
)
