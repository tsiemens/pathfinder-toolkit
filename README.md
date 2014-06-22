# Pathfinder Toolkit App [![Google Play](http://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=com.lateensoft.pathfinder.toolkit)

Pathfinder Toolkit is an Android application for use with the Pathfinder Roleplaying Game

## Contribution Guidelines
- This app is publicly released on Google Play, so all contributions must not disrupt the data of current users.
- Feel free to fork, submit pull requests, and submit issues. **Please check for duplicates first**.
- Please let us know if you are working on an issue by commenting on it.
- The working branch for public release is master. 
- All release versions have tags for reference, and their release APKs are available for testing updates.
- General refactoring is appreciated as much as feature implementation.

## Environment Setup Notes
- The JUnit4 jar must be specified in the dependency list ABOVE the Android SDK, so it overrides the default JUnit libraries.
- Mark the Robolectric library as "Provided", not "Compile" (This is IntelliJ terminology) in the dependency list. Otherwise, the android support libraries will conflict on compilation.

## Features:
- Complete character sheet manager for all your characters.
- Campaign party manager for GMs to keep track of their party members’ stats.
- Integrated initiative/encounter tracker to quickly make instances for your party.
- GM party stat roller to perform mass rolls for skill checks.
- Character ability score calculator
- Emergency virtual dice set (for when you forget your dice set at home!)

## Background
This started as a private project, just as we were starting to program. Mistakes were made, but as we learned, we tried to fix the project as much as we could. 

Once it was in better condition, we open sourced, because the community is a really big part of Pathfinder, and without its help, we won't be able to satisfy as many players.

## Community Use Policy
This application uses trademarks and/or copyrights owned by Paizo Publishing, LLC, which are used under Paizo's Community Use Policy. We are expressly prohibited from charging you to use or access this content. This application is not published, endorsed, or specifically approved by Paizo Publishing. For more information about Paizo's Community Use Policy, please visit [paizo.com/communityuse](http://paizo.com/paizo/about/communityuse). For more information about Paizo Publishing and Paizo products, please visit [paizo.com](http://paizo.com/paizo).

## License

* [Open Game License version 1.0a](http://paizo.com/pathfinderRPG/prd/openGameLicense.html)
