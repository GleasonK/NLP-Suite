#Natural Language Processing Suite
_this is currently a work in progress, only sentence tagging is functional_

Publicly hosted Natural Language Processing code API for use in web and mobile
apps.

### Demo

Try the demo [here][http://nlp-suite.appspot.com/tagger.html].

### Use and Examples

Simply place a request to http://nlp-suite.appspot.com/q=_words_ using HTTP GET or POST.
For example:

	http://nlp-suite.appspot.com/api/tagger?q=I+am+Kevin+Gleason
	// I/PP am/VBP Kevin/NP Gleason/NP

Tags follow the Penn TreeBank standards.




