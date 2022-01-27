- 1.0.2 (27 Jan. 2022):

  - Fixed persistent leaves' distance never being calculated when 'ignorePersistentLeaves' was enabled.
  - This prevented observers from triggering when observing persistent leaves whose distance should have changed, 
    which is used in some redstone contraptions.
  - Persistent leaves now calculate distance based on both non-persistent and persistent leaves, 
    while non-persistent leaves still ignore persistent leaves (if the config is enabled).

- 1.0.1 (15 Jan. 2022): 
  - Fixed translations downloading on servers.
  - Warnings will no longer be logged when Mod Menu and/or Cloth Config aren't present.
- 1.0 (6 Jan. 2022): First release!
- 0.1 (6 Jan. 2022): Rebranded to Leaves Us In Peace. Add features.