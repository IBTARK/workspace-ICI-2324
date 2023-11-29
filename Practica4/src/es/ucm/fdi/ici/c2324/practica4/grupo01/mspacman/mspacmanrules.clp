;FACTS ASSERTED BY GAME INPUT

(deftemplate MSPACMAN
	(slot levelUp (type SYMBOL))
	(slot combo (type SYMBOL)))
	
(deftemplate PPILL
	(slot closest (type NUMBER))
	(slot accessible (type SYMBOL))
	(slot close (type SYMBOL))
	(slot blocked (type SYMBOL)))
	
(deftemplate EDIBLE
	(slot nearestDist (type NUMBER))
	(slot attack (type SYMBOL))
	(slot attackClose (type SYMBOL))
	(slot nearestNextJuntDist (type NUMBER))
	(slot nextJuntDist (type NUMBER)))
	
(deftemplate CHASING
	(slot dangerLevel (type NUMBER)))
	
(deftemplate PILLS
	(slot few (type SYMBOL)))
	
(deftemplate ACTION
	(slot id) 
	(slot info (default ""))
	(slot priority (type NUMBER))
	(slot strategy (type SYMBOL)) 
) 
	
	
;MAIN RULES

(defrule neutral
	
	=>
	(assert (ACTION (id neutral) (priority 1)))
)

(defrule huir
	
	=>
	(assert (ACTION (id huir) (priority 5)))
)

(defrule perseguir
	
	=>
	(assert (ACTION (id perseguir) (priority 2)))
)

(defrule kamikazePill
	
	=>
	(assert (ACTION (id kamikazePill) (priority 3)))
)

(defrule kamikazeFantasma
	
	=>
	(assert (ACTION (id kamikazeFantasma) (priority 4)))
)

;STRATEGY RULES