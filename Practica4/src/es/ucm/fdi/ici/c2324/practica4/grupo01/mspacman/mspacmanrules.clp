;FACTS ASSERTED BY GAME INPUT

(deftemplate MSPACMAN
	(slot combo (type SYMBOL)))
	
(deftemplate PPILL
	(slot accessible (type SYMBOL))
	(slot close (type SYMBOL))
	(slot blocked (type SYMBOL)))
	
(deftemplate EDIBLE
	(slot nearestDist (type NUMBER))
	(slot attack (type SYMBOL))
	(slot attackClose (type SYMBOL))
	(slot nearestNextJunctDist (type NUMBER)))
	
(deftemplate CHASING
	(slot dangerLevel (type NUMBER))
	(slot danger (type SYMBOL)))
	
(deftemplate PILLS
	(slot few (type SYMBOL)))
	
(deftemplate ACTION
	(slot id) 
	(slot info (default ""))
	(slot priority (type NUMBER))
	(slot strategy (type SYMBOL)) 
) 

(deftemplate ALMOST_ACTION
	(slot id) 
	(slot priority (type NUMBER))
)
	
	
;INITIAL FACTS

(deffacts neutral
	(ALMOST_ACTION (id neutral) (priority 10))
)
	
;MAIN RULES

(defrule huir
	(CHASING (danger true))
	=>
	(assert (ALMOST_ACTION (id huir) (priority 30)))
)

(defrule perseguir
	(EDIBLE (attack true))
	=>
	(assert (ALMOST_ACTION (id perseguir) (priority 20)))
)

(defrule kamikazePill
	(PILLS (few true))
	=>
	(assert (ACTION (id kamikazePill) (priority 40)))
)

(defrule kamikazeFantasma
	(MSPACMAN (combo true))
	(EDIBLE (attackClose true))
	=>
	(assert (ACTION (id kamikazeFantasma) (priority 50)))
)

;HUIR RULES

(defrule huirFantasma
	(ALMOST_ACTION (id huir) (priority ?p))
	=>
	(assert (ACTION (id huir) (priority (+ ?p 1)) (strategy HUIR_DE_FANTASMA)))
)

(defrule huirVariosFantasmas
	(ALMOST_ACTION (id huir) (priority ?p))
	(CHASING (dangerLevel ?dl)) (test (> ?dl 1))
	=>
	(assert (ACTION (id huir) (priority (+ ?p 2)) (strategy HUIR_VARIOS_FANTASMAS)))
)

(defrule huirHaciaPPill
	(ALMOST_ACTION (id huir) (priority ?p))
	(CHASING (dangerLevel ?dl)) (test (> ?dl 1))
	(PPILL (close true) (blocked false))
	=>
	(assert (ACTION (id huir) (priority (+ ?p 4)) (strategy HUIR_HACIA_PPILL)))
)

(defrule huirRodeandoAPPill
	(ALMOST_ACTION (id huir) (priority ?p))
	(CHASING (dangerLevel ?dl)) (test (> ?dl 1))
	(PPILL (close true) (accessible true))
	=>
	(assert (ACTION (id huir) (priority (+ ?p 3)) (strategy HUIR_RODEANDO_PPILL)))
)

;PERSEGUIR RULES

(defrule perseguirDirecto
	(ALMOST_ACTION (id perseguir) (priority ?p))
	=>
	(assert (ACTION (id perseguir) (priority (+ ?p 1)) (strategy PERSEGUIR)))
)

(defrule flanquear
	(ALMOST_ACTION (id perseguir) (priority ?p))
	(EDIBLE (nearestDist ?nd)) (!= ?nd nil)
	(EDIBLE (nearestNextJunctDist ?nnjd)) (!= ?nnjd nil)
	(test (>= ?nd ?nnjd))
	=>
	(assert (ACTION (id perseguir) (priority (+ ?p 2)) (strategy FLANQUEAR)))
)

;NEUTRAL RULES

(defrule buscarPills
	(ALMOST_ACTION (id neutral) (priority ?p))
	=>
	(assert (ACTION (id neutral) (priority (+ ?p 1)) (strategy BUSCAR_PILLS)))
)

(defrule evitarPPill
	(ALMOST_ACTION (id neutral) (priority ?p))
	(PPILL (close true))
	=>
	(assert (ACTION (id neutral) (priority (+ ?p 2)) (strategy EVITAR_PPILL)))
)


