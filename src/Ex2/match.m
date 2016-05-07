function [ num_matches,matches,dist_vals ] = match(image1, image2, distRatio)
%match Find matches between image1 and image2

    % Find SIFT keypoints for each image
    [im1, des1, loc1] = sift(image1);
    [im2, des2, loc2] = sift(image2);

    % For each descriptor in the first image, select its match to second image.
    des2t = des2';                          % Precompute matrix transpose
    matches_indexes = zeros(size(des1,1), 1);
    all_dist_vals = zeros(size(des1,1), 1);
    for i = 1 : size(des1,1)
       dotprods = des1(i,:) * des2t;        % Computes vector of dot products
       [vals,indx] = sort(acos(dotprods));  % Take inverse cosine and sort results

       % Check if nearest neighbor has angle less than distRatio times 2nd.
       if (vals(1) < distRatio * vals(2))
          matches_indexes(i) = indx(1);
          all_dist_vals(i) = vals(1) / vals(2);
       else
          matches_indexes(i) = 0;
          all_dist_vals(i) = 0;
       end
    end
    num_matches = sum(matches_indexes > 0);
    matches = zeros(num_matches, 4);
    dist_vals = zeros(num_matches, 1);
    k = 1;
    for i = 1:size(matches_indexes, 1)
        j = matches_indexes(i);
        if (j > 0)
            matches(k, :) = [loc1(i,1), loc1(i,2), loc2(j, 1),...
                loc2(j,2)];
            dist_vals(k) = all_dist_vals(i);
            k = k +1;
        end
    end
  
end

