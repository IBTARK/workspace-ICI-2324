
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