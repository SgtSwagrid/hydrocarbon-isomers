# Counting hydrocarbon isomers,
## The world's best combinatorics problem.

An efficient computational solution for counting structural isomers of pure straight-chain alkanes, written in Java.

[Alkane and isomer table.](https://en.wikipedia.org/wiki/List_of_straight-chain_alkanes)

| Alkane | Carbons | Isomers | Alkane | Carbons | Isomers
| --- | --- | --- | --- | --- | --- |
| Methane | 1 | 1 | Undecane | 11 | 159
| Ethane | 2 | 1 | Dodecane | 12 | 355
| Propane | 3 | 1 | Tridecane | 13 | 802
| Butane | 4 | 2 | Tetradecane | 14 | 1,858
| Pentane | 5 | 3 | Pentadecane | 15 | 4,347
| Hexane | 6 | 5 | Hexadecane | 16 | 10,359
| Heptane | 7 | 9 | Heptadecane | 17 | 24,894
| Octane | 8 | 18 | Octadecane | 18 | 60,523
| Nonane | 9 | 35 | Nonadecane | 19 | 148,284
| Hexane | 10 | 75 | Icosane | 20 | 366,319

#### Counting rooted trees

First, one should note that all isomers of straight-chain alkanes (which are what is being considered here) are trees (cycle-free).  This is because introducing cycles changes the number of hydrogens, thus disqualifying such molecules from isomer status. Note however that introducing double/triple bonds allows the exchange of these bonds for cycles, making this more general case much more difficult.

My approach uses [integer partitions](https://en.wikipedia.org/wiki/Partition_%28number_theory%29) and the [multi-choose](https://en.wikipedia.org/wiki/Multiset) function to recursively enumerate rooted subtrees. That is, the number of rooted trees of size n is defined in terms of the number of ways one could generate some number of smaller subtrees using the n-1 vertices which remain after designating a root.

#### Counting unrooted trees

I define a new notion of 'tree centre', distinct from the traditional definition (so as to avoid any notion of tree depth), but with similar uniqueness properties. A node is a centre (or bi-centre) if no adjacent subtree exceeds ⌊n/2⌋ nodes. This allows unrooted trees to be counted by only considering trees which are centrally-rooted.

To address any skepticism as to whether my notion of centre is actually both unique and complete, observe that (a) moving along any edge away from a centre would produce, back in the direction travelled from, a tree of size greater than ⌊n/2⌋, and that (b) any tree without a centre could not possibly have a finite number of nodes.

#### Future work

This method could be adapted to work with non-pure alkanes (for instance, haloalkanes) by paritioning on all combinations of elements (rather than just the number of carbon atoms), and specifying the required (no longer maximum) number of bonds for each element (including hydrogen, which could no longer be omitted).

In fact, for the simple case where only a single halogen atom is added, the problem actually becomes far easier, since the carbon to which the halogen is attached can serve as the root of the tree, and so one can simply count rooted trees.

However, in general, where double/triple bonds, or loops (which, for our purposes, are equivalent) are considered, the problem becomes much more difficult. One would likely have to resort to generating and storing every possible rooted graph, indexed by some unique key, so that newly generated graphs can quickly, in O(1) time, be compared to all previously generated graphs, so that duplicates may be discarded.

#### Limitations

This method does not account for stereoisomers (especially since I'm not a chemist, and don't really know what these are).
This method also does not account for molecules which could not exist (despite being a valid graph), due to the limited physical space available to tightly-wound structures. Simulations to test which structures are possible are beyond the scope of this project.

#### Motivation

I created this project for no other reason than that it seemed like an interesting challenge. While browsing Wikipedia, I found that there were 366,319 isomers for icosane, and I wanted to figure out how to calculate this number, challenging some friends to do the same. It was a good challenge, more so than I originally anticipated. Is this even useful? I don't know, probably not (atleast not outside of a mathematical context). If you're into chemistry, and this is even remotely related to something useful, I'd love to hear about it.

#### Related work

Like I said, I also challenged some friends to try this too. Here's a similar solution, written in Python:

https://github.com/TipTaco/AlkaneIsomerSolver

Additionally, the following is a very interesting paper I found on the topic, after finishing my implementation:

https://prod-ng.sandia.gov/techlib-noauth/access-control.cgi/2004/040960.pdf
