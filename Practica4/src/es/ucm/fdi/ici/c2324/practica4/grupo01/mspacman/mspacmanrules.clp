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
	(slot nearestNextJunctDist (type NUMBER))
	(slot nextJuntDist (type NUMBER)))
	
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
	
	
;MAIN RULES

(defrule neutral
=>
	(assert (ACTION (id neutral) (priority 10)))
)

(defrule huir
	(CHASING (danger true))
	=>
	(assert (ACTION (id huir) (priority 30)))
)

(defrule perseguir
	(EDIBLE (attack true))
	=>
	(assert (ACTION (id perseguir) (priority 20)))
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
	(ACTION (id huir) (priority ?p))
	=>
	(assert (ACTION (id huir) (priority (+ ?p 1)) (strategy HUIR_DE_FANTASMA)))
)

(defrule huirVariosFantasmas
	(ACTION (id huir) (priority ?p))
	(CHASING (dangerLevel ?dl)) (test (> ?dl 1))
	=>
	(assert (ACTION (id huir) (priority (+ ?p 2)) (strategy HUIR_VARIOS_FANTASMAS)))
)

(defrule huirHaciaPPill
	(ACTION (id huir) (priority ?p))
	(CHASING (dangerLevel ?dl)) (test (> ?dl 1))
	(PPILL (close true) (blocked false))
	=>
	(assert (ACTION (id huir) (priority (+ ?p 4)) (strategy HUIR_HACIA_PPILL)))
)

(defrule huirRodeandoAPPill
	(ACTION (id huir) (priority ?p))
	(CHASING (dangerLevel ?dl)) (test (> ?dl 1))
	(PPILL (close true) (accessible true))
	=>
	(assert (ACTION (id huir) (priority (+ ?p 3)) (strategy HUIR_RODEANDO_PPILL)))
)

;PERSEGUIR RULES

(defrule perseguirDirecto
	(ACTION (id perseguir) (priority ?p))
	=>
	(assert (ACTION (id perseguir) (priority (+ ?p 1)) (strategy PERSEGUIR)))
)

(defrule flanquear
	(ACTION (id huir) (priority ?p))
	(EDIBLE (nearestDist ?nd))
	(EDIBLE (nearestNextJunctDist ?nnjd))
	(test (>= ?nd ?nnjd))
	=>
	(assert (ACTION (id perseguir) (priority (+ ?p 2)) (strategy FLANQUEAR)))
)

;NEUTRAL RULES

(defrule buscarPills
	(ACTION (id neutral) (priority ?p))
	=>
	(assert (ACTION (id neutral) (priority (+ ?p 1)) (strategy BUSCAR_PILLS)))
)

(defrule evitarPPill
	(ACTION (id neutral) (priority ?p))
	(PPILL (close true))
	=>
	(assert (ACTION (id neutral) (priority (+ ?p 2)) (strategy EVITAR_PPILL)))
)